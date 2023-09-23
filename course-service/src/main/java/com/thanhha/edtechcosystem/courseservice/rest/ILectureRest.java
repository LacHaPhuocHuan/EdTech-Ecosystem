package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/courses/lectures")
public interface ILectureRest {
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<?> getLecture(@PathVariable("id") String id);

    @GetMapping("/by-course/{id}")
    public ResponseEntity<?> getLectureByCourse(@PathVariable("id") Long idCourse);

    //POST
    @PostMapping({"","/"})
    public ResponseEntity<?> createLecture(@RequestBody LectureDto lectureDto);

    //PUT
    @PatchMapping({"/{id}"})
    public ResponseEntity<?> updateLecture(@RequestBody LectureDto lectureDto, @PathVariable("id") String id);




}
