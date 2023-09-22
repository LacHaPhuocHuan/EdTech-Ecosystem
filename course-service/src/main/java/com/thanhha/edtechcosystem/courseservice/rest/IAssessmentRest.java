package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.AssessmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/courses/assessment")
public interface IAssessmentRest {
    //GET

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssessment(@PathVariable("id") String id);

    @GetMapping("/{idCourse}")
    public ResponseEntity<?> getAllAssessment(@PathVariable("idCourse") String idCourse);

    //POST

    @PostMapping({"","/"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    public ResponseEntity<?> createAssessment(@RequestBody AssessmentDto assessmentDto);

    //Update
    @PreAuthorize("hasAnyRoles('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PatchMapping({"","/"})
    public ResponseEntity<?> updateAssessment(@RequestBody AssessmentDto assessmentDto);


}
