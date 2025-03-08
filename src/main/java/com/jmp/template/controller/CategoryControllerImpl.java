package com.jmp.template.controller;

import com.jmp.template.dto.response.CategoryTreeDto;
import com.jmp.template.model.Category;
import com.jmp.template.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryServiceT;

    /**
     * Создание категории.
     *
     * @param name     Название категории.
     * @param parentId ID родительской категории (опционально).
     * @return Созданная категория.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId
    ) {
        return categoryServiceT.createCategory(name, parentId);
    }

    /**
     * Удаление категории.
     *
     * @param id ID категории.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryServiceT.deleteCategory(id);
    }

    /**
     * Обновление категории.
     *
     * @param id          ID категории.
     * @param newName     Новое название категории (опционально).
     * @param newParentId ID нового родителя (опционально, -1 для отвязки).
     * @return Обновленная категория.
     */
    @PutMapping("/{id}")
    public Category updateCategory(
            @PathVariable Long id,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) Long newParentId
    ) {
        return categoryServiceT.updateCategory(id, newName, newParentId);
    }

    /**
     * Получение дерева категорий.
     *
     * @param id ID категории.
     * @return Дерево категорий.
     *
     * Переименуй сука(tree)
     *
     */
    @GetMapping("/{id}/tree")
    public CategoryTreeDto getCategoryTree(@PathVariable Long id) {
        return categoryServiceT.getCategoryTree(id);
    }
}

