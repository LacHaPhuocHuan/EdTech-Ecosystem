package com.thanhha.edtechcosystem.courseservice.entity;

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

@Entity
@Table(name = "Student_Assessment")
public class StudentAssessment {
    @EmbeddedId
    private StudentAssessmentKey studentAssessmentKey;
    @ManyToOne()
    @MapsId("userId")
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @MapsId("assessmentId")
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private int trueAnswer;


}
