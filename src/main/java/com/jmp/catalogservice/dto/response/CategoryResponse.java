package com.jmp.catalogservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final List<CategoryResponse> children;
}
