package com.jmp.template.controller;

import com.jmp.template.dto.response.CategoryTreeDto;
import com.jmp.template.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Category API", description = "API для работы с категориями")
public interface CategoryController {

    @Operation(summary = "Создать категорию", description = "Создает новую категорию.")
    @ApiResponse(responseCode = "201", description = "Категория успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "409", description = "Категория с таким именем уже существует")
    Category createCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId
    );

    @Operation(summary = "Получить дерево категорий", description = "Возвращает дерево категорий, начиная с указанной.")
    @ApiResponse(responseCode = "200", description = "Дерево категорий успешно получено")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    CategoryTreeDto getCategoryTree(@PathVariable Long id);

    @Operation(summary = "Обновить категорию", description = "Обновляет название или родителя категории.")
    @ApiResponse(responseCode = "200", description = "Категория успешно обновлена")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @ApiResponse(responseCode = "409", description = "Категория с таким именем уже существует")
    Category updateCategory(
            @PathVariable Long id,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) Long newParentId
    );

    @Operation(summary = "Удалить категорию", description = "Удаляет категорию и все её дочерние категории.")
    @ApiResponse(responseCode = "204", description = "Категория успешно удалена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    void deleteCategory(@PathVariable Long id);
}
