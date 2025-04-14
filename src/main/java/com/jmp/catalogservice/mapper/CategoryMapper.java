package com.jmp.catalogservice.mapper;

import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import com.jmp.catalogservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "children", ignore = true)
    CategoryResponse toDto(Category entity);

    @Mapping(target = "children", source = "children")
    CategoryResponse toDtoWithChildren(Category entity, List<CategoryResponse> children);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    Category toEntity(CategoryRequest dto);
}