package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.CategoryDto;

public interface ICategoryService {
    CategoryDto getById(Long id);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);
}
