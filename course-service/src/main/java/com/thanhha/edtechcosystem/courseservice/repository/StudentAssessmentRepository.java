package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.StudentAssessment;
import com.thanhha.edtechcosystem.courseservice.entity.StudentAssessmentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAssessmentRepository extends JpaRepository<StudentAssessment, StudentAssessmentKey> {
    List<StudentAssessment> findByStudentId(String idStudent);
}
