package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.InstructorDto;
import com.thanhha.edtechcosystem.courseservice.entity.Instructor;

public interface IInstructorService {
    InstructorDto getById(String id);
}
