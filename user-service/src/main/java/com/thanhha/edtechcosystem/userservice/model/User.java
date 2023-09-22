package com.thanhha.edtechcosystem.userservice.model;

import com.thanhha.edtechcosystem.userservice.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tbUser")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Date createAt;
    private Boolean isNonClock;
    private Boolean isNonExpired;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "education_profile_id", referencedColumnName = "id")
    private EducationProfile educationProfile;

}
