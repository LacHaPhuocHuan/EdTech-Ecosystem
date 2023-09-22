package com.thanhha.edtechcosystem.userservice.rest.restImpl;

import com.thanhha.edtechcosystem.userservice.dto.*;
import com.thanhha.edtechcosystem.userservice.model.User;
import com.thanhha.edtechcosystem.userservice.rest.EducationProfileRest;
import com.thanhha.edtechcosystem.userservice.rest.UserRest;
import com.thanhha.edtechcosystem.userservice.security.MyUserDetailsService;
import com.thanhha.edtechcosystem.userservice.security.Role;
import com.thanhha.edtechcosystem.userservice.service.UserService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserRestImpl implements UserRest {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MyUserDetailsService userDetailsService;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    @Override
    public ResponseEntity<?> getMyInfo() {
        var userInfo=userService.getUserById(userDetailsService.getCurrentUser().getId());
        kafkaTemplate.send("notification", userInfo);
        EntityModel<UserDto> entityModel=setLinkTo(userInfo);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getAll(int page, int size) {
        var userPage=userService.getAllUser(page, size);
        UserPageModel userPageModel=modelMapper.map(userPage, UserPageModel.class);
        userPageModel.setUserList(userPage.getUserDtoList()
                        .stream()
                        .map(this::setLinkTo)
                        .collect(Collectors.toList())
                );
         return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(userPageModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getById(String idUser) {
        var userInfo=userService.getUserById(idUser);
        EntityModel<UserDto> entityModel=setLinkTo(userInfo);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> addUser(AdditionalUserRequest additionalUser) {
        var addedUser=userService.addUser(additionalUser);
        EntityModel<UserDto> entityModel=setLinkTo(addedUser);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updateUser(UpdateUserRequest updateUser) {
        var update=userService.updateUser(updateUser, userDetailsService.getCurrentUser().getId());
        EntityModel<UserDto> entityModel=setLinkTo(update);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deactivateAccount(String idUser) {
        UserDto userDto= userService.deactivateAccount(idUser);
        EntityModel<UserDto> entityModel=setLinkTo(userDto);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deactivateMyAccount(String password) {
        UserDto userDto= userService.deactivateMyAccount(userDetailsService.getCurrentUser().getId(), password);
        EntityModel<UserDto> entityModel=setLinkTo(userDto);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Operation is success.")
                        .data(entityModel)
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> resetPassword(String oldPassword, String newPassword, String confirmPassword) {
        userService.resetPassword(oldPassword,newPassword,confirmPassword, userDetailsService.getCurrentUser().getId());
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("The refreshed password is success.")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }



    private EntityModel<UserDto> setLinkTo(UserDto user){
        EntityModel<UserDto> userDtoEntityModel=EntityModel.of(user);
        userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(UserRest.class).withRel("update-patch"));
        userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRest.class).deactivateMyAccount("xxx")).withRel("deactivate-post"));
        userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRest.class).getAll(1,10)).withRel("get-all"));
        userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRest.class).getById(user.getId())).slash(user.getId()).withRel("get-by-id"));
        userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRest.class).resetPassword("","","")).withRel("reset-password-put"));
        if(user.getRole().equals(Role.STUDENT)) {
            try {
                userDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EducationProfileRest.class).getProfileById(user.getEducationProfileId())).slash(user.getEducationProfileId()).withRel("my-profile-get"));
            }catch (Exception e){
                log.error("User(id : {}) don't have educational profile. ", user.getId());
            }
            //course taken...
        }

        return userDtoEntityModel;
    }
}
