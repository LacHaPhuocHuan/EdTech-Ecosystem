package com.thanhha.edtechcosystem.userservice.rest;

import com.thanhha.edtechcosystem.userservice.dto.AuthenticatedUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.RegisterUserRequest;
import com.thanhha.edtechcosystem.userservice.exception.CredentialValidException;
import com.thanhha.edtechcosystem.userservice.exception.ServerIncorrectException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users/auth/")
public interface AuthenticationRest {

    @PostMapping("registration")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserRequest registerUser) throws CredentialValidException, ServerIncorrectException;

    @PostMapping("authentication")
    public ResponseEntity<?> signIn(@RequestBody AuthenticatedUserRequest authenticatedUser);

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@NonNull HttpServletRequest request);
}
