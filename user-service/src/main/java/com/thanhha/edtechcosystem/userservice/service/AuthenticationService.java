package com.thanhha.edtechcosystem.userservice.service;

import com.thanhha.edtechcosystem.userservice.dto.AuthenticatedUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.RegisterUserRequest;
import com.thanhha.edtechcosystem.userservice.exception.CredentialValidException;
import com.thanhha.edtechcosystem.userservice.exception.ServerIncorrectException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;

public interface AuthenticationService {
    String signUp(RegisterUserRequest registerUser) throws CredentialValidException, ServerIncorrectException;

    String signIn(AuthenticatedUserRequest authenticatedUser) ;

    String refreshToken(HttpServletRequest request);
}
