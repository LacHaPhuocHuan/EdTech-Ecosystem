package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbLecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = (100))
    private String id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;
    @OneToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private LectureVideo video;
    private String content;
    @OneToMany(mappedBy = "lecture")
    private List<Document> documents;

}
