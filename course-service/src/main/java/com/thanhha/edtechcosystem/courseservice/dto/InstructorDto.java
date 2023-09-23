package com.thanhha.edtechcosystem.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDto {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
}
