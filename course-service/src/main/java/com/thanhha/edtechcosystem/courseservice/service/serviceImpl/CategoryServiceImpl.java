package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.CategoryDto;
import com.thanhha.edtechcosystem.courseservice.entity.Category;
import com.thanhha.edtechcosystem.courseservice.repository.CategoryRepository;
import com.thanhha.edtechcosystem.courseservice.service.ICategoryService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public CategoryDto getById(Long id) {
        return findById(id);
    }

    private CategoryDto findById(Long id) {
        //todo... use redis cache
        return modelMapper.map(categoryRepository.findById(id).orElseThrow(NotFoundException::new),CategoryDto.class);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category
                =modelMapper.map(categoryDto, Category.class);
        category.setId(null);
        var saveCategory=categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        if(categoryDto.getId()==null)
            throw new BadRequestException("");
        Category category
                =categoryRepository.findById(categoryDto.getId()).orElseThrow(NotFoundException::new);
        if(!Objects.isNull(categoryDto.getName()))
            category.setName(categoryDto.getName());
        if(!Objects.isNull(categoryDto.getDescription()))
            category.setDescription(categoryDto.getDescription());
        var saveCategory=categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll() {
        List<Category> categories=categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category,CategoryDto.class)).toList();
    }
}
