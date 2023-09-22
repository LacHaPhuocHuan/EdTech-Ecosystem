package com.thanhha.edtechcosystem.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private HttpStatus httpStatus;
    @JsonProperty("jwtToken")
    private Object jwt;
}
