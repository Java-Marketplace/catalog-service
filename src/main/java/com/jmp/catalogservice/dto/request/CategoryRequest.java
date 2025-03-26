package com.jmp.catalogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank
    private final String name;
    private final Long parentId;
}
