package com.jmp.catalogservice.controller;

import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import com.jmp.catalogservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@RequestBody @Valid CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest request
    ) {
        return categoryService.updateCategory(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    @GetMapping("/{id}/tree")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryTree(@PathVariable Long id) {
        return categoryService.getCategoryTree(id);
    }

    @Override
    @GetMapping("/parents")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getAllRootCategories() {
        return categoryService.getAllRootCategories();
    }
}

