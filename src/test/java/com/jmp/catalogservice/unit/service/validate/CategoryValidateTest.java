package com.jmp.catalogservice.unit.service.validate;

import com.jmp.catalogservice.exception.DuplicateCategoryException;
import com.jmp.catalogservice.repository.CategoryRepository;
import com.jmp.catalogservice.service.validate.CategoryValidate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryValidateTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryValidate categoryValidate;

    @Test
    void validateNameUniquenessShouldReturnTrue() {
        String categoryName = "test";
        when(categoryRepository.existsByName(categoryName)).thenReturn(false);

        assertDoesNotThrow(() -> categoryValidate.validateNameUniqueness(categoryName));

        verify(categoryRepository).existsByName(categoryName);
    }

    @Test
    void validateNameUniquenessShouldThrowException() {
        String categoryName = "test";
        when(categoryRepository.existsByName(categoryName)).thenReturn(true);

        DuplicateCategoryException exception = assertThrows(
                DuplicateCategoryException.class,
                () -> categoryValidate.validateNameUniqueness(categoryName)
        );
        assertEquals("Duplicate category name detected: " + categoryName, exception.getMessage());

        verify(categoryRepository).existsByName(categoryName);
    }
}
