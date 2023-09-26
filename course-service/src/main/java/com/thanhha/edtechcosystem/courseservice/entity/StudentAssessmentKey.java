package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class StudentAssessmentKey implements Serializable {
    @Column(name = "student_id")
    private String userId;
    @Column(name = "assessment_id")
    private Long assessmentId;
}
