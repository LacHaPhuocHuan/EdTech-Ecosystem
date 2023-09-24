package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionnaireId(Long courseId);
}
