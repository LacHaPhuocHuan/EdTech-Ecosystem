package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbEnrollment")
public class StudentCourse {
    @Id
    private String enrolledCode;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Date enrolledDate;

    private Status status;
    private Float GPA;
    private Date completedDate;
}
