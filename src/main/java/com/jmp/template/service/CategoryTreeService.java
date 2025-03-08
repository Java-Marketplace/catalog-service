package com.jmp.template.service;

import com.jmp.template.dto.response.CategoryTreeDto;
import com.jmp.template.exception.EntityNotFoundException;
import com.jmp.template.model.Category;
import com.jmp.template.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryTreeService {

    private final CategoryRepository categoryRepository;

    /**
     * Построение дерева категорий, начиная с категории с указанным ID.
     */
    public CategoryTreeDto buildCategoryTree(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID %d не найдена".formatted(id)));
        return buildCategoryTreeDto(category);
    }

    /**
     * Рекурсивный метод для построения дерева категорий.
     */
    private CategoryTreeDto buildCategoryTreeDto(Category category) {
        List<CategoryTreeDto> children = categoryRepository.findByParentId(category.getId())
                .stream()
                .map(this::buildCategoryTreeDto)
                .toList();

        return new CategoryTreeDto(category.getId(), category.getName(), children);
    }
}
