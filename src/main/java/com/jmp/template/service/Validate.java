package com.jmp.template.service;

import com.jmp.template.exception.DuplicateCategoryException;
import com.jmp.template.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Validate {
    private final CategoryRepository categoryRepository;

    /**
     * Проверка уникальности имени категории.
     */
    public void validateNameUniqueness(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateCategoryException(
                    "Категория с именем '%s' уже существует".formatted(name)
            );
        }
    }
}
