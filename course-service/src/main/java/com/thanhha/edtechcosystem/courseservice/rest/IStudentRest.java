package com.thanhha.edtechcosystem.courseservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/courses/students")
public interface IStudentRest {
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") String id);

    @GetMapping("/{idCourse}")
    public ResponseEntity<?> getStudentByIdCourse(@PathVariable("idCourse") String idCourse);




}
