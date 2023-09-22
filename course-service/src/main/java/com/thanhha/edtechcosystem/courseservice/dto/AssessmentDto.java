package com.thanhha.edtechcosystem.courseservice.dto;

import com.thanhha.edtechcosystem.courseservice.entity.AssessmentStatus;
import com.thanhha.edtechcosystem.courseservice.entity.Course;
import com.thanhha.edtechcosystem.courseservice.entity.Question;
import com.thanhha.edtechcosystem.courseservice.entity.Questionnaire;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssessmentDto {
    private Long id;

    private String title;

    private String courseId;

    private String description;

    private List<Question> questions;

    private AssessmentStatus status;
}
