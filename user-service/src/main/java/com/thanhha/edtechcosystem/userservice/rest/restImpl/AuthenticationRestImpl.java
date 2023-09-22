package com.thanhha.edtechcosystem.userservice.rest.restImpl;

import com.thanhha.edtechcosystem.userservice.dto.AuthResponse;
import com.thanhha.edtechcosystem.userservice.dto.AuthenticatedUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.RegisterUserRequest;

import com.thanhha.edtechcosystem.userservice.exception.CredentialValidException;
import com.thanhha.edtechcosystem.userservice.exception.ServerIncorrectException;
import com.thanhha.edtechcosystem.userservice.rest.AuthenticationRest;
import com.thanhha.edtechcosystem.userservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRestImpl implements AuthenticationRest {
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<?> signUp(RegisterUserRequest registerUser) throws CredentialValidException, ServerIncorrectException {
        var jwtToken=authenticationService.signUp(registerUser);
        log.info("Registration is successfully! Email: {}", registerUser.getEmail());
        return ResponseEntity.ok().body(
                AuthResponse.builder()
                .jwt(jwtToken)
                .message("Registration is successfully.")
                .httpStatus(HttpStatus.OK).build());
    }

    @Override
    public ResponseEntity<?> signIn(AuthenticatedUserRequest authenticatedUser) {
        var jwtToken= authenticationService.signIn(authenticatedUser);
        log.info("Authentication is successfully! Email: {}", authenticatedUser.getEmail());
        return ResponseEntity.ok().body(
                AuthResponse.builder()
                        .jwt(jwtToken)
                        .message("Authentication is successfully.")
                        .httpStatus(HttpStatus.OK).build());
    }

    @Override
    public ResponseEntity<?> refreshToken(@NonNull HttpServletRequest request) {
        var jwtToken=authenticationService.refreshToken(request);
        return ResponseEntity.ok(AuthResponse.builder()
                .jwt(jwtToken)
                .message("Refreshed Token is successfully!")
                .httpStatus(HttpStatus.OK)
                .build());
    }
}
