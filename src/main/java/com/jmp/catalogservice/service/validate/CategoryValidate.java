package com.jmp.catalogservice.service.validate;

import com.jmp.catalogservice.exception.DuplicateCategoryException;
import com.jmp.catalogservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryValidate {
    private final CategoryRepository categoryRepository;

    public void validateNameUniqueness(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateCategoryException(name);
        }
    }
}
