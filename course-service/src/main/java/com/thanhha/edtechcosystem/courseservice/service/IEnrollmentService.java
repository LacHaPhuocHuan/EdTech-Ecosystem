package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.Certificate;

public interface IEnrollmentService {
    Certificate completeCourse(String code);
}
