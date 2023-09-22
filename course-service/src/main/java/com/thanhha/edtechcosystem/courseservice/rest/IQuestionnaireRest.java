package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.QuestionnaireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/courses/questions")
public interface IQuestionnaireRest {
    @GetMapping({"/",""})
    public ResponseEntity<?> getQuestionnaire();

    @PostMapping("")
    public ResponseEntity<?> createQuestionnaire(@RequestBody QuestionnaireDto questionnaireDto);
}
