package com.thanhha.edtechcosystem.courseservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursesDto {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date createDate;
    private Date expiredDate;
    private String teacherId;
    private Long categoryId;
}
