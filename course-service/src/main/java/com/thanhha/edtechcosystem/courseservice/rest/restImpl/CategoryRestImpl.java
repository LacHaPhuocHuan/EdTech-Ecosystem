package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.CategoryDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.rest.ICategoryRest;
import com.thanhha.edtechcosystem.courseservice.service.ICategoryService;
import com.thanhha.edtechcosystem.courseservice.service.serviceImpl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryRestImpl implements ICategoryRest {

    private final ICategoryService iCategoryService;



    @Override
    public ResponseEntity<?> getCategory(Long id) {
        var category=iCategoryService.getById(id);
        return ResponseEntity.ok().body(
                DataResponse.builder().data(hateoas(category)).message("OK").build()
        );
    }

    @Override
    public ResponseEntity<?> createCategory(CategoryDto categoryDto) {
        var category=iCategoryService.createCategory(categoryDto);
        return ResponseEntity.ok().body(
                DataResponse.builder().data(hateoas(category)).message("OK").build()
        );
    }

    @Override
    public ResponseEntity<?> updateCategory(CategoryDto categoryDto) {
        var category=iCategoryService.updateCategory(categoryDto);
        return ResponseEntity.ok().body(
                DataResponse.builder().data(hateoas(category)).message("OK").build()
        );
    }

    private EntityModel<CategoryDto> hateoas(CategoryDto categoryDto){
        EntityModel<CategoryDto> entityModel= EntityModel.of(categoryDto);
        //todo...
        return entityModel;
    }
    @Override
    public ResponseEntity<?> getAll() {
        List<CategoryDto> categories=iCategoryService.getAll();
        return ResponseEntity.ok().body(
                DataResponse.builder().data(
                        categories.stream().map(this::hateoas).toList()
                ).message("OK").build()
        );
    }


}
