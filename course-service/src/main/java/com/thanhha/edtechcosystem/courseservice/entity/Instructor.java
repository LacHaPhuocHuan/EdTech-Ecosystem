package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbInstructor")
public class Instructor {
    @Id
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;

}
