package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Long > {
    List<Assessment> findByCourseId(Long idCourse);
}
