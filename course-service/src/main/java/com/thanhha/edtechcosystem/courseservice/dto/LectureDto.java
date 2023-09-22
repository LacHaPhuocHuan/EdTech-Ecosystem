package com.thanhha.edtechcosystem.courseservice.dto;

import com.thanhha.edtechcosystem.courseservice.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDto {
    private String id;
    private Long courseId;
    private String title;
    private String content;

}
