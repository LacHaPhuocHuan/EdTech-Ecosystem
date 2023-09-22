package com.thanhha.edtechcosystem.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbTentativeJwtToken")
public class JwtTokenDto implements Serializable {
    @Id
    private Long id;
    @Column(length = 1000)
    private String token;
    private Instant expireDate;
    private String userId;
    private boolean disable;
}
