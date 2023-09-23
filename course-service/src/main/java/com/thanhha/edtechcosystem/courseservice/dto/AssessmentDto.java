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
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssessmentDto {
    @NotNull
    private Long id;

    private String title;

    private String courseId;

    private String description;

    private List<QuestionDto> questions;

    private AssessmentStatus status;
}
