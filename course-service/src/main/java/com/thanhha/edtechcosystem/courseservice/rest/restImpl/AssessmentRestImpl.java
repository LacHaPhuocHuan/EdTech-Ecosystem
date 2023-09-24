package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.AssessmentDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.rest.IAssessmentRest;
import com.thanhha.edtechcosystem.courseservice.service.IAssessmentService;
import jakarta.ws.rs.BadRequestException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssessmentRestImpl implements IAssessmentRest {
    private final IAssessmentService assessmentService;
    @Override
    public ResponseEntity<?> getAssessment(Long id) {
        AssessmentDto assessment=null;
        try {
             assessment = assessmentService.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.ok(DataResponse.builder().data(hateoas(assessment)).message("").build());
    }

    @Override
    public ResponseEntity<?> getAllAssessment(String idCourse) {
        List<AssessmentDto> assessments=assessmentService.getAllOnCourse(idCourse);
        List<EntityModel<AssessmentDto>> entityModels=assessments.stream()//
                .map(this::hateoas).toList();
        return ResponseEntity.ok(DataResponse.builder().data(entityModels).message("").build());
    }

    @Override
    public ResponseEntity<?> createAssessment(AssessmentDto assessmentDto) {
        var assessment=assessmentService.createAssessment(assessmentDto);
        return ResponseEntity.ok(DataResponse.builder().data(hateoas(assessment)).message("").build());
    }

    @Override
    public ResponseEntity<?> updateAssessment(AssessmentDto assessmentDto) {
        var assessment=assessmentService.updateAssessment(assessmentDto);
        return ResponseEntity.ok(DataResponse.builder().data(hateoas(assessment)).message("").build());
    }
    private EntityModel<AssessmentDto> hateoas(AssessmentDto object){
        EntityModel<AssessmentDto> entityModel= EntityModel.of(object);
        //todo...
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAssessmentRest.class).getAssessment(object.getId())).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAssessmentRest.class).createAssessment(null)).withRel("POST"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAssessmentRest.class).getAllAssessment("1")).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAssessmentRest.class).updateAssessment(null)).withRel("PATCH"));
        return entityModel;
    }

}
