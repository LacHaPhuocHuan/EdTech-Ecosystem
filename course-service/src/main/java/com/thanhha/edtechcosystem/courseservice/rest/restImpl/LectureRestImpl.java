package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import com.thanhha.edtechcosystem.courseservice.entity.Lecture;
import com.thanhha.edtechcosystem.courseservice.rest.ILectureRest;
import com.thanhha.edtechcosystem.courseservice.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LectureRestImpl implements ILectureRest {
    private final ILectureService iLectureService;
    @Override
    public ResponseEntity<?> getLecture(String id) {
        var lecture= iLectureService.getById(id);
        return ResponseEntity.ok(DataResponse.builder()
                        .data(hateoas(lecture))
                        .message("")
                .build());
    }

    @Override
    public ResponseEntity<?> getLectureByCourse(Long idCourse) {
        List<LectureDto> lecture= iLectureService.getLectureByCourse(idCourse);
        return ResponseEntity.ok(DataResponse.builder()
                .data(
                        lecture
                        .stream()
                        .map(this::hateoas).collect(Collectors.toList())
                )
                .message("")
                .build());
    }

    @Override
    public ResponseEntity<?> createLecture(LectureDto lectureDto) {
        var lecture= iLectureService.createLecture(lectureDto);
        return ResponseEntity.ok(DataResponse.builder()
                .data(hateoas(lecture))
                .message("")
                .build());
    }

    @Override
    public ResponseEntity<?> updateLecture(LectureDto lectureDto, String id) {
        var lecture= iLectureService.updateLecture(lectureDto, id);
        return ResponseEntity.ok(DataResponse.builder()
                .data(hateoas(lecture))
                .message("")
                .build());
    }
    private EntityModel<LectureDto> hateoas(LectureDto lectureDto){
        EntityModel<LectureDto> entityModel=EntityModel.of(lectureDto);
        //todo...
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureRest.class).getLecture(lectureDto.getId())).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureRest.class).getLectureByCourse(lectureDto.getCourseId())).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureRest.class).createLecture(null)).withRel("POST"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureRest.class).updateLecture(null, lectureDto.getId())).withRel("PATCH"));
        return entityModel;
    }
}
