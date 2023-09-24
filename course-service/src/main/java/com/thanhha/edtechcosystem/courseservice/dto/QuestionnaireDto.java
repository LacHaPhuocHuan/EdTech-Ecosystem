package com.thanhha.edtechcosystem.courseservice.dto;

import com.thanhha.edtechcosystem.courseservice.entity.Assessment;
import com.thanhha.edtechcosystem.courseservice.entity.Question;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireDto {
    private Long id;
    private String description;
    private Long assessmentId;
    private List<QuestionDto> questions;
}
