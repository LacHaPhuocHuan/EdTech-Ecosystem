package com.thanhha.edtechcosystem.userservice.rest.restImpl;

import com.thanhha.edtechcosystem.userservice.dto.CourseTakenDto;
import com.thanhha.edtechcosystem.userservice.dto.DataResponse;
import com.thanhha.edtechcosystem.userservice.rest.CourseTakenRest;
import com.thanhha.edtechcosystem.userservice.security.MyUserDetailsService;
import com.thanhha.edtechcosystem.userservice.service.CourseTakenService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseTakenRestImpl implements CourseTakenRest {
    private final CourseTakenService courseTakenService;
    private final MyUserDetailsService userDetailsService;

    @Override
    public ResponseEntity<?> getAll(int page, int size) {
        List<CourseTakenDto> courseList=courseTakenService.getPage(page,size);
        List<EntityModel<CourseTakenDto>> entityModels=courseList.stream()
                .map(this::hateoasCourseTaken).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .data(entityModels)
                .httpStatus(HttpStatus.OK)
                .message("Operation is successful.").build());
    }

    private EntityModel<CourseTakenDto> hateoasCourseTaken(CourseTakenDto courseTakenDto) {
        EntityModel<CourseTakenDto> entityModel=EntityModel.of(courseTakenDto);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CourseTakenRest.class).getCourseTakenById(courseTakenDto.getId())).withRel("Get-TakenCourse"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CourseTakenRest.class).getAll(1,10)).withRel("Get-TakenCourse"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CourseTakenRest.class).getAllByIdUser(1,10, "id_user")).withRel("Get-By_IdUser"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRestImpl.class).getById(courseTakenDto.getIdUser())).withRel("get-user"));
        return entityModel;
    }

    @Override
    public ResponseEntity<?> getCourseTakenById(Long idCourseTaken) {
        var course=courseTakenService.findById(idCourseTaken);

        return ResponseEntity.ok(DataResponse.builder()
                .data(hateoasCourseTaken(course))
                .httpStatus(HttpStatus.OK)
                .message("Operation is successful.").build());
    }

    @Override
    public ResponseEntity<?> getAllByIdUser(int page, int size, String id) {
        if(page<=0)
            page=1;
        List<CourseTakenDto> courseList=courseTakenService.getPageByIdUser(page,size,id);
        List<EntityModel<CourseTakenDto>> entityModels=courseList.stream()
                .map(this::hateoasCourseTaken).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .data(entityModels)
                .httpStatus(HttpStatus.OK)
                .message("Operation is successful.").build());
    }

    @Override
    public ResponseEntity<?> getMyCourses() {
        List<CourseTakenDto> courseList=courseTakenService.getPageByIdUser(1,100,userDetailsService.getCurrentUser().getId());
        List<EntityModel<CourseTakenDto>> entityModels=courseList.stream()
                .map(this::hateoasCourseTaken).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .data(entityModels)
                .httpStatus(HttpStatus.OK)
                .message("Operation is successful.").build());
    }


    @Override
    public ResponseEntity<?> completeCourse(CourseTakenDto courseTakenDto) {
        var course=courseTakenService.createTakenCourse(courseTakenDto,courseTakenDto.getIdUser());

        return ResponseEntity.ok(DataResponse.builder()
                .data(hateoasCourseTaken(course))
                .httpStatus(HttpStatus.OK)
                .message("Operation is successful.").build());
    }
}
