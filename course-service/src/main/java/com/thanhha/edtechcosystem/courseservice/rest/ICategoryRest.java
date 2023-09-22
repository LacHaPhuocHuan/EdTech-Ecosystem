package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/courses/categories")
public interface ICategoryRest {
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id);

    //POST
    @PostMapping({"","/"})
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto);

    //UPDATE
    @PatchMapping({"","/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto);
}
