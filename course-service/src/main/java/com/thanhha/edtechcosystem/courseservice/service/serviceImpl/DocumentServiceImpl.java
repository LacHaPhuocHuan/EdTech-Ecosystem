package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DocumentDto;
import com.thanhha.edtechcosystem.courseservice.entity.Document;
import com.thanhha.edtechcosystem.courseservice.entity.Lecture;
import com.thanhha.edtechcosystem.courseservice.repository.DocumentRepository;
import com.thanhha.edtechcosystem.courseservice.repository.LectureRepository;
import com.thanhha.edtechcosystem.courseservice.service.IDocumentService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements IDocumentService {
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;
    private final LectureRepository lectureRepository;
    @Override
    public DocumentDto getDocumentById(String id) {
        //todo ... find on cache
        return modelMapper.map(documentRepository.findById(id).orElseThrow(NotFoundException::new), DocumentDto.class);
    }

    @Override
    public DocumentDto createDocument(DocumentDto documentDto) {
        Document document=modelMapper.map(documentDto, Document.class);
        Lecture lecture=lectureRepository.findById(documentDto.getLectureId()).orElseThrow(NotFoundException::new);
        document.setLecture(lecture);
        var saveDocument=documentRepository.save(document);
        return modelMapper.map(saveDocument,DocumentDto.class);
    }
}
