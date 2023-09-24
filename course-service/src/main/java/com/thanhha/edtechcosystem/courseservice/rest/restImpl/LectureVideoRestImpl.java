package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import com.thanhha.edtechcosystem.courseservice.dto.LectureVideoDto;
import com.thanhha.edtechcosystem.courseservice.rest.ILectureVideoRest;
import com.thanhha.edtechcosystem.courseservice.service.ILectureVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LectureVideoRestImpl implements ILectureVideoRest {
    private final ILectureVideoService iLectureVideoService;
    @Override
    public ResponseEntity<?> getVideo(Long id) {
        var video=iLectureVideoService.getById(id);
        return ResponseEntity.ok(DataResponse.builder().data(hateoas(video)).message("").build());
    }

    @Override
    public ResponseEntity<?> uploadVideo(MultipartFile file,String id) {
        var video=iLectureVideoService.store(file, id);
        log.info("UPLOAD FILE {}", video.getUrl());
        return ResponseEntity.ok(DataResponse.builder().data(hateoas(video)).message("").build());
    }

    @Override
    public ResponseEntity<Resource> serveFile(String filename) {

        Resource file = iLectureVideoService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    private EntityModel<LectureVideoDto> hateoas(LectureVideoDto lectureVideoDto){
        EntityModel<LectureVideoDto> entityModel= EntityModel.of(lectureVideoDto);
        //todo..
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureVideoRest.class).getVideo(lectureVideoDto.getId())).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ILectureVideoRest.class).uploadVideo(null,lectureVideoDto.getLectureId())).withRel("POST"));

        return entityModel;
    }
}
