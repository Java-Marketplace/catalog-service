package com.jmp.catalogservice.service;

import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import com.jmp.catalogservice.exception.CategoryNotFoundException;
import com.jmp.catalogservice.exception.CircularDependencyException;
import com.jmp.catalogservice.mapper.CategoryMapper;
import com.jmp.catalogservice.model.Category;
import com.jmp.catalogservice.repository.CategoryRepository;
import com.jmp.catalogservice.service.validate.CategoryValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidate categoryValidate;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        categoryValidate.validateNameUniqueness(request.getName());

        Category category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            Category parent = getCategoryEntityById(request.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category savedCategory = repository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("Getting category by ID: {}", id);
        return categoryMapper.toDto(getCategoryEntityById(id));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category ID {} with data: {}", id, request);
        Category category = getCategoryEntityById(id);


        if (!request.getName().equals(category.getName())) {
            categoryValidate.validateNameUniqueness(request.getName());
            category.setName(request.getName());
        }

        Map<Long, Runnable> actions = new HashMap<>();
        actions.put(-1L, () -> category.setParent(null));
        actions.put(category.getId(), () -> {
            throw new CircularDependencyException();
        });
        actions.put(null, () -> category.setParent(category.getParent()));

        if (actions.containsKey(request.getParentId())) {
            actions.get(request.getParentId()).run();
        } else {
            category.setParent(getCategoryEntityById(request.getParentId()));
        }

        Category updatedCategory = repository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category ID: {}", id);
        repository.delete(getCategoryEntityById(id));
    }

    @Override
    public List<CategoryResponse> getAllRootCategories() {
        log.info("Getting all root categories");
        return repository.findByParentIsNull().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryTree(Long id) {
        log.info("Getting category tree for ID: {}", id);
        Category category = getCategoryEntityById(id);

        List<Category> childCategories = repository.findByParentId(id);
        List<CategoryResponse> childrens = new ArrayList<>();

        for (Category child : childCategories) {
            CategoryResponse childDto = getCategoryTree(child.getId());
            childrens.add(childDto);
        }

        return categoryMapper.toDtoWithChildren(category, childrens);
    }

    private Category getCategoryEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }
}