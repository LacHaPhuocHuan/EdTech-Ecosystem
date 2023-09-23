package com.thanhha.edtechcosystem.courseservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/courses/instructors")
public interface IInstructorRest {
    @GetMapping("/{id}")
    public ResponseEntity<?> getInstructorById(@PathVariable("id") String id);
}
