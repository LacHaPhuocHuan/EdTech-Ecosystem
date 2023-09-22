package com.thanhha.edtechcosystem.userservice.dto;


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
public class JwtTokenDto implements Serializable {
    private Long id;
    private String token;
    private Instant expireDate;
    private String userId;
    private boolean disable;
}
