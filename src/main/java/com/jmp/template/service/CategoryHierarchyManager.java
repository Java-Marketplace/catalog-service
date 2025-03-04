package com.jmp.template.service;

import com.jmp.template.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHierarchyManager {
    private final CategoryValidator validator;

    public void updateParent(Category category, Category newParent) {
        validator.checkCircularDependency(category, newParent);

        // Удаление из старого родителя
        if (category.getParent() != null) {
            category.getParent().getChildren().remove(category);
        }

        // Установка нового родителя
        category.setParent(newParent);

        // Добавление к новому родителю
        if (newParent != null) {
            newParent.getChildren().add(category);
        }
    }
}

