package com.jmp.template.service;

import com.jmp.template.exception.CircularDependencyException;
import com.jmp.template.exception.DuplicateCategoryException;
import com.jmp.template.model.Category;
import com.jmp.template.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public void validateNameUniqueness(String name, Category parent) {
        boolean exists = parent != null
                ? categoryRepository.existsByNameAndParent(name, parent)
                : categoryRepository.existsByNameAndParentIsNull(name);

        if (exists) {
            throw new DuplicateCategoryException(
                    "Категория '%s' уже существует%s"
                            .formatted(name, parent != null
                                    ? " в категории '%s'".formatted(parent.getName())
                                    : "")
            );
        }
    }

    public void checkCircularDependency(Category category, Category newParent) {
        if (newParent == null) return;

        Category current = newParent;
        int depth = 0;
        while (current != null && depth++ < 100) {
            if (current.equals(category)) {
                throw new CircularDependencyException(
                        "Обнаружена циклическая зависимость: %s -> %s"
                                .formatted(newParent.getName(), category.getName())
                );
            }
            current = current.getParent();
        }
    }
}