package com.jmp.template.service;

import com.jmp.template.model.Category;
import com.jmp.template.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(String name, Long parentId) {
        Category category = new Category(name);

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));

            if (categoryRepository.existsByNameAndParent(name, parent)) {
                throw new IllegalStateException("Category name must be unique within parent");
            }

            parent.addChild(category);
        } else {
            if (categoryRepository.existsByNameAndParent(name, null)) {
                throw new IllegalStateException("Root category name must be unique");
            }
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategoryWithChildren(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    public List<Category> getFullTree() {
        return categoryRepository.findRootCategories();
    }
}
