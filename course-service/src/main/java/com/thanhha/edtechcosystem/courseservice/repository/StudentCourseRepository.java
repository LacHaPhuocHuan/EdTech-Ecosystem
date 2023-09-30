package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, String> {
    boolean existsByEnrolledCodeAndStudentId(String code, String id);
}
