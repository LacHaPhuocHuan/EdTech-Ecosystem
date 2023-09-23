package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.DocumentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/courses/documents")
public interface IDocumentRest {
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocument(@PathVariable("id") String id);
    @PostMapping("")
    public ResponseEntity<?> createDocument(@RequestBody DocumentDto documentDto);
}
