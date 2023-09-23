package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.AssessmentDto;

import java.util.List;

public interface IAssessmentService {
    AssessmentDto getById(String id);

    List<AssessmentDto> getAllOnCourse(String idCourse);

    AssessmentDto createAssessment(AssessmentDto assessmentDto);

    AssessmentDto updateAssessment(AssessmentDto assessmentDto);
}
