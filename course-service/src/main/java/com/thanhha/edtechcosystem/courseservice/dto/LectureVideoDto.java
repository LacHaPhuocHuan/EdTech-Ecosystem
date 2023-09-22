package com.thanhha.edtechcosystem.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LectureVideoDto {
    private Long id;
    private String title;
    private String url;
    private Date uploadDate;
    private String lectureId;
}
