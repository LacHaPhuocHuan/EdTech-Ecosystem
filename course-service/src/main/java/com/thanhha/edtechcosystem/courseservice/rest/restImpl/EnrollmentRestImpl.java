package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.rest.IEnrollmentRest;
import com.thanhha.edtechcosystem.courseservice.service.IEnrollmentService;
import com.thanhha.edtechcosystem.courseservice.service.serviceImpl.EnrollmentServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrollmentRestImpl implements IEnrollmentRest {

    private final IEnrollmentService iEnrollmentService;
    @Override
    public ResponseEntity<?> complete(String code) {
        var completeCourse=iEnrollmentService.completeCourse(code);
        return ResponseEntity.ok(
                DataResponse.builder()
                        .message("Congratulation. You completed this course")
                        .data(completeCourse)
                        .build()
        );
    }
}
