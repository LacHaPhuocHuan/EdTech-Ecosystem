package com.thanhha.edtechcosystem.courseservice.dto;

import com.thanhha.edtechcosystem.courseservice.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private Date birthDate;
    private Role role;
    private Date createAt;
    private Boolean isNonClock;
    private Boolean isNonExpired;
    private Long educationProfileId;


}

