package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.dto.DocumentDto;
import com.thanhha.edtechcosystem.courseservice.rest.IDocumentRest;
import com.thanhha.edtechcosystem.courseservice.service.IDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentRestImpl implements IDocumentRest {
    private final IDocumentService iDocumentService;
    @Override
    public ResponseEntity<?> getDocument(String id) {
        var document=iDocumentService.getDocumentById(id);
        return ResponseEntity.ok(DataResponse.builder()
                        .message("")
                        .data(hateoas(document))
                .build());
    }

    @Override
    public ResponseEntity<?> createDocument(DocumentDto documentDto) {
        var document=iDocumentService.createDocument(documentDto);
        return ResponseEntity.ok(DataResponse.builder()
                .message("")
                .data(hateoas(document))
                .build());
    }
    public EntityModel<DocumentDto> hateoas(DocumentDto documentDto){
        EntityModel<DocumentDto> entityModel=EntityModel.of(documentDto);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IDocumentRest.class).getDocument("abc")).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IDocumentRest.class).createDocument(null)).withRel("POST"));
        return entityModel;
    }

}
