package com.thanhha.edtechcosystem.courseservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentDto {
    private String id;
    private String url;
    private String title;
    private String lectureId;
}
