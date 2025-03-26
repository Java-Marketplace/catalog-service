package com.jmp.catalogservice.controller;

import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Category API", description = "API для работы с категориями")
public interface CategoryController {

    @Operation(summary = "Создать категорию", description = "Создает новую категорию.")
    @ApiResponse(responseCode = "201", description = "Категория успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "409", description = "Категория с таким именем уже существует")
    @PostMapping
    CategoryResponse createCategory(@RequestBody CategoryRequest request);

    @Operation(summary = "Получить категорию по ID",
            description = "Возвращает информацию о категории без дочерних элементов")
    @ApiResponse(responseCode = "200", description = "Категория найдена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/{id}")
    CategoryResponse getCategoryById(@PathVariable Long id);

    @Operation(summary = "Обновить категорию",
            description = "Обновляет название или родителя категории.")
    @ApiResponse(responseCode = "200", description = "Категория успешно обновлена")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @ApiResponse(responseCode = "409", description = "Категория с таким именем уже существует")
    @PutMapping("/{id}")
    CategoryResponse updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    );

    @Operation(summary = "Удалить категорию",
            description = "Удаляет категорию и все её дочерние категории.")
    @ApiResponse(responseCode = "204", description = "Категория успешно удалена")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable Long id);

    @Operation(summary = "Получить дерево категорий",
            description = "Возвращает дерево категорий, начиная с указанной.")
    @ApiResponse(responseCode = "200", description = "Дерево категорий успешно получено")
    @ApiResponse(responseCode = "404", description = "Категория не найдена")
    @GetMapping("/{id}/tree")
    CategoryResponse getCategoryTree(@PathVariable Long id);

    @Operation(summary = "Получить все root-категории",
            description = "Возвращает список категорий без родителя")
    @ApiResponse(responseCode = "200", description = "Список root-категорий получен")
    @GetMapping("/parents")
    List<CategoryResponse> getAllRootCategories();
}