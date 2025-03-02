package com.jmp.template.controller;

import com.jmp.template.model.Category;
import com.jmp.template.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Long> createCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId
    ) {
        Category category = categoryService.createCategory(name, parentId);
        return ResponseEntity
                .created(URI.create("/api/categories/" + category.getId()))
                .body(category.getId());
    }

    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryService.getFullTree();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryWithChildren(id);
    }
}
