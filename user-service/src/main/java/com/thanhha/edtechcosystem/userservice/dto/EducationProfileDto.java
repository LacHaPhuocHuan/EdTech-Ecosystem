package com.thanhha.edtechcosystem.userservice.dto;

import com.thanhha.edtechcosystem.userservice.model.EducationalLevel;
import com.thanhha.edtechcosystem.userservice.model.EducationalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EducationProfileDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Float GPA;
    private String userId;
    private String level;
    private String status;
}
