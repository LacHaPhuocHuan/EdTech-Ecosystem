package com.thanhha.edtechcosystem.courseservice.rest.restImpl;


import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<?> handleConflict(
            RuntimeException ex, WebRequest request) {

        return new ResponseEntity<>(
                DataResponse.builder()
                .message(ex.getMessage())
                .data(null).build(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(value
            = { InternalServerErrorException.class, ServerErrorException.class })
    protected ResponseEntity<?> handleBadRequest(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                DataResponse.builder()
                        .message(ex.getMessage())
                        .data(null).build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    @ExceptionHandler(value
            = {  UsernameNotFoundException.class, BadRequestException.class, NotFoundException.class, BadCredentialsException.class})
    protected ResponseEntity<?> handleBadRequest(
            Exception e, WebRequest request) {
        return new ResponseEntity<>(
                DataResponse.builder()
                        .message(e.getMessage())
                        .data(null).build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
