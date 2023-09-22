package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.Lecture;
import com.thanhha.edtechcosystem.courseservice.entity.LectureVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureVideoRepository extends JpaRepository<LectureVideo, Long> {
}
