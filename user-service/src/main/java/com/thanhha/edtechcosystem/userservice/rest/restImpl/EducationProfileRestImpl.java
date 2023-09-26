package com.thanhha.edtechcosystem.userservice.rest.restImpl;

import com.thanhha.edtechcosystem.userservice.dto.DataResponse;
import com.thanhha.edtechcosystem.userservice.dto.EducationProfileDto;
import com.thanhha.edtechcosystem.userservice.dto.UpdateProfileRequest;
import com.thanhha.edtechcosystem.userservice.rest.EducationProfileRest;
import com.thanhha.edtechcosystem.userservice.rest.UserRest;
import com.thanhha.edtechcosystem.userservice.security.MyUserDetailsService;
import com.thanhha.edtechcosystem.userservice.security.Role;
import com.thanhha.edtechcosystem.userservice.service.EducationProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EducationProfileRestImpl implements EducationProfileRest {
    private final MyUserDetailsService userDetailsService;
    private final EducationProfileService educationProfileService;
    @Override
    public ResponseEntity<?> getMyProfile() {
        var educationalProfile=educationProfileService.getMyProfile();
        EntityModel<EducationProfileDto> profileEntityModel= hateoasProfile(educationalProfile);

        return ResponseEntity.ok(DataResponse.builder()
                        .message("Operation is successful")
                        .data(profileEntityModel)
                        .httpStatus(HttpStatus.OK)
                .build());
    }

    private EntityModel<EducationProfileDto> hateoasProfile(EducationProfileDto educationalProfile) {
        EntityModel<EducationProfileDto> profileEntityModel=EntityModel.of(educationalProfile);
        Role myRole=userDetailsService.getCurrentUser().getRole();
        if(myRole.equals(Role.STUDENT)) {
            profileEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EducationProfileRest.class).getMyProfile()).withRel("my-profile-get"));
        }
        if(myRole.equals(Role.ADMIN)) {
            profileEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EducationProfileRest.class).getAll()).withRel("all-profile-get"));
        }

        profileEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRest.class).getById(educationalProfile.getUserId())).withRel("info-user-get"));
        return profileEntityModel;


    }


    @Override
    public ResponseEntity<?> getAll() {
        var educationalProfileList=educationProfileService.getAllProfile();
        List<EntityModel<EducationProfileDto>> entityModelList= educationalProfileList.stream().map(this::hateoasProfile).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .message("Operation is successful")
                .data(entityModelList)
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @Override
    public ResponseEntity<?> getProfileById(Long id) {
        var educationalProfile=educationProfileService.getProfileById(id);
        EntityModel<EducationProfileDto> profileEntityModel= hateoasProfile(educationalProfile);

        return ResponseEntity.ok(DataResponse.builder()
                .message("Operation is successful")
                .data(profileEntityModel)
                .httpStatus(HttpStatus.OK)
                .build());
    }

//    @Override
//    public ResponseEntity<?> updateProfileByUser(EducationProfileDto profileDto) {
//        return null;
//    }

    @Override
    public ResponseEntity<?> updateProfileByAdmin(UpdateProfileRequest profileDto, Long id) {
        var educationalProfile=educationProfileService.updateByAdmin(profileDto, id);
        EntityModel<EducationProfileDto> profileEntityModel= hateoasProfile(educationalProfile);

        return ResponseEntity.ok(DataResponse.builder()
                .message("Operation is successful")
                .data(profileEntityModel)
                .httpStatus(HttpStatus.OK)
                .build());    }
}
