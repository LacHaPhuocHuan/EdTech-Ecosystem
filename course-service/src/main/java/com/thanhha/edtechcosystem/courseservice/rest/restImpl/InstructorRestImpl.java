package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.rest.IInstructorRest;
import com.thanhha.edtechcosystem.courseservice.service.IInstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InstructorRestImpl implements IInstructorRest {
    private final IInstructorService iInstructorService;
    @Override
    public ResponseEntity<?> getInstructorById(String id) {
        var instructor=iInstructorService.getById(id);
        return ResponseEntity.ok(DataResponse.builder()
                        .message("")
                        .data(instructor)
                .build());
    }
}
