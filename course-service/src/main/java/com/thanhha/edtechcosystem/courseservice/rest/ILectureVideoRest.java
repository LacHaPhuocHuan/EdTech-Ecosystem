package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/courses/video")
public interface ILectureVideoRest {
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideo(@PathVariable("id") Long id);

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("")
    public ResponseEntity<?> createVideo(@RequestBody LectureDto lectureDto);


}
