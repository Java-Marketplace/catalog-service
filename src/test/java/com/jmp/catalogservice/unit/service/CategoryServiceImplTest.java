package com.jmp.catalogservice.unit.service;

import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import com.jmp.catalogservice.exception.CategoryNotFoundException;
import com.jmp.catalogservice.mapper.CategoryMapper;
import com.jmp.catalogservice.model.Category;
import com.jmp.catalogservice.repository.CategoryRepository;
import com.jmp.catalogservice.service.CategoryServiceImp;
import com.jmp.catalogservice.service.validate.CategoryValidate;
import com.jmp.catalogservice.support.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest extends BaseUnitTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryValidate categoryValidate;

    @InjectMocks
    private CategoryServiceImp categoryServiceImp;

    private final Long testId = 1L;
    private final String testName = "Electronics";
    private final CategoryRequest testRequest =
            new CategoryRequest(testName, null);
    private final Category testCategory =
            Category.builder().id(testId).name(testName).parent(null).build();
    private final CategoryResponse testResponse =
            new CategoryResponse(testId, testName, null);

    @Test
    void testCreateCategory() {
        when(categoryMapper.toEntity(testRequest)).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponse);

        CategoryResponse categoryResponse = categoryServiceImp.createCategory(testRequest);

        assertNotNull(categoryResponse);
        assertEquals(testId, categoryResponse.getId());
        assertEquals(testName, categoryResponse.getName());

        verify(categoryValidate).validateNameUniqueness(testName);
        verify(categoryMapper).toDto(testCategory);
        verify(categoryRepository).save(testCategory);
        verify(categoryMapper).toDto(testCategory);

    }

    @Test
    void testCreateCategoryWithParent() {
        Long parentId = 2L;
        Category parentCategory = Category.builder().id(parentId).name("Parent").build();
        CategoryRequest requestWithParent = new CategoryRequest("Child", parentId);
        Category childCategory = Category.builder().
                id(3L).name("Child")
                .parent(parentCategory)
                .build();
        CategoryResponse expectedResponse = new CategoryResponse(3L, "Child", List.of());

        when(categoryMapper.toEntity(requestWithParent)).thenReturn(childCategory);
        when(categoryRepository.save(childCategory)).thenReturn(childCategory);
        when(categoryMapper.toDto(childCategory)).thenReturn(expectedResponse);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));

        CategoryResponse response = categoryServiceImp.createCategory(requestWithParent);

        assertNotNull(response);
        assertEquals("Child", response.getName());
        verify(categoryRepository).findById(parentId);
    }

    @Test
    void getCategoryShouldReturnCategoryWhenExists() {
        when(categoryRepository.findById(testId)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponse);

        CategoryResponse categoryResponse = categoryServiceImp.getCategoryById(testId);

        assertNotNull(categoryResponse);
        assertEquals(testId, categoryResponse.getId());

        verify(categoryRepository).findById(testId);
        verify(categoryMapper).toDto(testCategory);
    }

    @Test
    void getCategoryShouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryServiceImp.getCategoryById(testId));

        verify(categoryRepository).findById(testId);
    }

    @Test
    void updateCategoryShouldUpdateCategoryAndReturnUpdatedCategory() {
        String updateName = "updateName";

        CategoryRequest updateRequest = new CategoryRequest(updateName, null);
        Category originalCategory = Category.builder()
                .id(testId)
                .name(testName)
                .parent(null)
                .build();
        Category updatedCategory = Category.builder()
                .id(testId)
                .name(updateName)
                .parent(null)
                .build();
        CategoryResponse expectedResponse = new CategoryResponse(testId, updateName, null);

        when(categoryRepository.findById(testId)).thenReturn(Optional.of(originalCategory));
        when(categoryRepository.save(argThat(c ->
                c.getId().equals(testId) &&
                        c.getName().equals(updateName))
        )).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedResponse);

        CategoryResponse result = categoryServiceImp.updateCategory(testId, updateRequest);

        assertNotNull(result);
        assertEquals(updateName, result.getName());
        assertEquals(testId, result.getId());

        verify(categoryValidate).validateNameUniqueness(updateName);
        verify(categoryRepository).save(argThat(c ->
                c.getId().equals(testId) &&
                        c.getName().equals(updateName)));
        verify(categoryMapper).toDto(updatedCategory);
    }

    @Test
    void testUpdateCategoryWithNewParent() {
        Long newParentId = 3L;
        Category newParent = Category.builder()
                .id(newParentId)
                .name("New Parent")
                .build();
        CategoryRequest request = new CategoryRequest("Updated", newParentId);

        Category originalCategory = Category.builder()
                .id(testId)
                .name(testName)
                .parent(testCategory)
                .build();

        Category updatedCategory = Category.builder()
                .id(testId)
                .name("Updated")
                .parent(newParent)
                .build();

        CategoryResponse expectedResponse = new CategoryResponse(testId, "Updated", List.of());

        when(categoryRepository.findById(testId)).thenReturn(Optional.of(originalCategory));
        when(categoryRepository.findById(newParentId)).thenReturn(Optional.of(newParent));
        when(categoryRepository.save(any())).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedResponse);

        CategoryResponse response = categoryServiceImp.updateCategory(testId, request);

        assertNotNull(response);
        assertEquals("Updated", response.getName());
        verify(categoryRepository).findById(newParentId);
    }

    @Test
    void updateCategoryShouldResetParentWhenParentIdIsMinusOne() {
        Long categoryId = 1L;
        String updatedName = "Updated Category";
        Long originalParentId = 2L;

        Category originalParent = Category.builder()
                .id(originalParentId)
                .name("Original Parent")
                .build();
        Category originalCategory = Category.builder()
                .id(categoryId)
                .name("Original Category")
                .parent(originalParent)
                .build();

        CategoryRequest updateRequest = new CategoryRequest(updatedName, -1L);

        Category updatedCategory = Category.builder()
                .id(categoryId)
                .name(updatedName)
                .parent(null)
                .build();

        CategoryResponse expectedResponse =
                new CategoryResponse(categoryId, updatedName, List.of());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(originalCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedResponse);

        CategoryResponse result = categoryServiceImp.updateCategory(categoryId, updateRequest);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals(updatedName, result.getName());
        assertTrue(result.getChildren().isEmpty());

        verify(categoryRepository).save(argThat(c ->
                c.getId().equals(categoryId) &&
                        c.getName().equals(updatedName) &&
                        c.getParent() == null
        ));
    }

    @Test
    void deleteCategoryShouldDeleteCategoryWhenExists() {
        when(categoryRepository.findById(testId)).thenReturn(Optional.of(testCategory));
        categoryServiceImp.deleteCategory(testId);
        verify(categoryRepository).delete(testCategory);

    }

    @Test
    void getAllRootCategoriesShouldReturnRootCategories() {
        List<Category> rootCategory = List.of(testCategory);
        when(categoryRepository.findByParentIsNull()).thenReturn(rootCategory);
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponse);

        List<CategoryResponse> allRootCategories = categoryServiceImp.getAllRootCategories();

        assertFalse(allRootCategories.isEmpty());
        assertEquals(1, allRootCategories.size());
        assertEquals(testId, allRootCategories.getFirst().getId());

        verify(categoryRepository).findByParentIsNull();
    }

    @Test
    void getCategoryTreeShouldBuildTreeStructure() {
        Long childId = 2L;
        Category childCategory = Category.builder()
                .id(childId)
                .name("Child")
                .parent(testCategory)
                .build();

        when(categoryRepository.findById(testId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findById(childId)).thenReturn(Optional.of(childCategory));
        when(categoryRepository.findByParentId(testId)).thenReturn(List.of(childCategory));
        when(categoryMapper.toDtoWithChildren(any(), any())).thenAnswer(i -> {
            Category category = i.getArgument(0);
            List<CategoryResponse> children = i.getArgument(1);
            return new CategoryResponse(category.getId(), category.getName(), children);
        });

        CategoryResponse result = categoryServiceImp.getCategoryTree(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertNotNull(result.getChildren());
        assertEquals(1, result.getChildren().size());
        assertEquals(childId, result.getChildren().getFirst().getId());
    }

}
