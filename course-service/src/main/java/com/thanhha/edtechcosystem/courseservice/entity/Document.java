package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbDocument")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 100)
    private String id;
    private String url;
    private String title;
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
