package com.thanhha.edtechcosystem.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbEducationProfile")
public class EducationProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "educationProfile")
    private User user;
    private Float GPA;
    @Enumerated(EnumType.STRING)
    private EducationalLevel level;
    @Enumerated(EnumType.STRING)
    private EducationalStatus status;

}
