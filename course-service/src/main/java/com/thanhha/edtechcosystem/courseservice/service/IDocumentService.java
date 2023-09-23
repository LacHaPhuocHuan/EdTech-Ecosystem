package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.DocumentDto;

public interface IDocumentService {
    DocumentDto getDocumentById(String id);

    DocumentDto createDocument(DocumentDto documentDto);
}
