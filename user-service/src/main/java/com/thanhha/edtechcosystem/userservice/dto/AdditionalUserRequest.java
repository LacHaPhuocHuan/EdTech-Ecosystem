package com.thanhha.edtechcosystem.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private Date birthDate;
}
