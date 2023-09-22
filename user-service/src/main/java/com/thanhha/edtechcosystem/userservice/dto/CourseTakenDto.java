package com.thanhha.edtechcosystem.userservice.dto;

import com.thanhha.edtechcosystem.userservice.model.Grade;
import com.thanhha.edtechcosystem.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseTakenDto {
    private Long id;
    private String name;
    private Grade Grade;
    private Long idCourse;
    private String idUser;
    private LocalDate attendAt;
    private LocalDate completeAt;

}
