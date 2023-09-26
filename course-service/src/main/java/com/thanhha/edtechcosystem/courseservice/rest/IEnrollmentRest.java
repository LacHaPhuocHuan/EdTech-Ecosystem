package com.thanhha.edtechcosystem.courseservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/courses/enrollment")
public interface IEnrollmentRest {

    //todo...
    @RequestMapping("/completion/{idCourse}")
    public ResponseEntity<?> complete(@PathVariable("idCourse") String idCourse);
}
