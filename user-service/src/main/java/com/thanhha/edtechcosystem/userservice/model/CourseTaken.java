package com.thanhha.edtechcosystem.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbCourseTaken")
@Builder
public class CourseTaken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Grade Grade;
    private Long idCourse;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    private LocalDate attendAt;
    private LocalDate completeAt;


}
