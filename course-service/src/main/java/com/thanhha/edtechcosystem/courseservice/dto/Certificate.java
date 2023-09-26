package com.thanhha.edtechcosystem.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    private String enrolledCode;
    private String courseTitle;
    private String studentId;
    private Date enrolledDate;
    private float GPA;
    private Date completedDate;
    private String classification;
    private Long courseId;

}
