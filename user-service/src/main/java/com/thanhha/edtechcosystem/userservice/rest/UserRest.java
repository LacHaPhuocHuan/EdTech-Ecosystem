package com.thanhha.edtechcosystem.userservice.rest;

import com.thanhha.edtechcosystem.userservice.dto.AdditionalUserRequest;

import com.thanhha.edtechcosystem.userservice.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users/info")
public interface UserRest {
    @GetMapping({"","/"})
    //User lay thong tin cua minh
    public ResponseEntity<?> getMyInfo();
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    );
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") String idUser);

    @PostMapping({"","/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //Resets pass for user
    public ResponseEntity<?> addUser(@RequestBody AdditionalUserRequest additionalUser);

    @PatchMapping({"","/"})
    //User se tu update thong tin cua minh
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUser);

    @PutMapping("/deactivation/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateAccount(@PathVariable("id") String idUser);

    @PutMapping("/deactivation")
    public ResponseEntity<?> deactivateMyAccount(@RequestParam("password") String password);

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("old-password") String oldPassword,
                                           @RequestParam("new-password") String newPassword,
                                           @RequestParam("confirm-new-password") String confirmPassword);



}
