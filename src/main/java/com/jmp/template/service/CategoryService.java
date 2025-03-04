package com.jmp.template.service;

import com.jmp.template.model.Category;
import com.jmp.template.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator validator;
    private final CategoryHierarchyManager hierarchyManager;

    @Transactional
    public Category createCategory(String name, Long parentId) {
        Category parent = parentId != null
                ? getCategoryById(parentId)
                : null;

        validator.validateNameUniqueness(name, parent);

        Category category = new Category(name);
        hierarchyManager.updateParent(category, parent);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, String newName, Long newParentId) {
        Category category = getCategoryById(id);
        Category newParent = newParentId != null
                ? getCategoryById(newParentId)
                : null;

        // Обновление имени
        if (newName != null && !newName.isBlank()) {
            validator.validateNameUniqueness(newName, newParent != null ? newParent : category.getParent());
            category.setName(newName);
        }

        // Обновление родителя
        if (!Objects.equals(category.getParent(), newParent)) {
            hierarchyManager.updateParent(category, newParent);
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        hierarchyManager.updateParent(category, null);
        categoryRepository.delete(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Категория с ID %d не найдена".formatted(id)
                ));
    }

    public List<Category> getChildren(Long parentId) {
        return parentId != null
                ? getCategoryById(parentId).getChildren()
                : categoryRepository.findByParentIsNull();
    }
}

