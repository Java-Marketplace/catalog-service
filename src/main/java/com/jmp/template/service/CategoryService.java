package com.jmp.template.service;

import com.jmp.template.dto.response.CategoryTreeDto;
import com.jmp.template.exception.EntityNotFoundException;
import com.jmp.template.model.Category;
import com.jmp.template.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryTreeService categoryTreeService;
    private final Validate validate;

    /**
     * Создание категории.
     * Если передан parentId, категория привязывается к родителю.
     * Если parentId не передан, категория создается как корневая.
     */
    @Transactional
    public Category createCategory(String name, Long parentId) {
        log.info("Создание категории: name={}, parentId={}", name, parentId);
        validate.validateNameUniqueness(name); // Проверка уникальности имени

        Category parent = parentId != null
                ? getCategoryById(parentId)
                : null;

        Category category = Category.builder().
                                    name(name).
                                    build();
        category.setParent(parent);

        return categoryRepository.save(category);
    }

    /**
     * Удаление категории.
     * Удаляет категорию и все её дочерние категории (каскадное удаление).
     */
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Удаление категории с ID: {}", id);
        Category category = getCategoryById(id);
        categoryRepository.delete(category); // Каскадное удаление благодаря orphanRemoval = true
    }

    /**
     * Обновление категории.
     * Если newParentId = -1, категория отвязывается от родителя.
     * Если newParentId = null, связь с родителем не меняется.
     * Если newName != null, обновляется имя категории.
     */
    @Transactional
    public Category updateCategory(Long id, String newName, Long newParentId) {
        log.info("Обновление категории: id={}, newName={}, newParentId={}", id, newName, newParentId);
        Category category = getCategoryById(id);

        // Обновление имени
        if (newName != null && !newName.isBlank()) {
            validate.validateNameUniqueness(newName); // Проверка уникальности нового имени
            category.setName(newName);
        }

        // Обновление родителя
        if (newParentId != null) {
            if (newParentId == -1) {
                // Отвязка от родителя
                category.setParent(null);
            } else {
                // Привязка к новому родителю
                Category newParent = getCategoryById(newParentId);
                category.setParent(newParent);
            }
        }

        return categoryRepository.save(category);
    }

    /**
     * Поиск категории и всех её дочерних категорий.
     */
    public CategoryTreeDto getCategoryTree(Long id) {
        log.info("Поиск категории и её дочерних категорий: id={}", id);
        return categoryTreeService.buildCategoryTree(id);
    }

    /**
     * Получение категории по ID.
     * Если категория не найдена, выбрасывается исключение.
     */
    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория с ID %d не найдена".formatted(id)
                ));
    }

}