package com.jmp.template.dto.response;

import java.util.ArrayList;
import java.util.List;
/*Поменять на ДТО категории*/
public record CategoryTreeDto(Long id,
                              String name,
                              List<CategoryTreeDto> children) {
    public CategoryTreeDto(Long id, String name) {
        this(id, name, new ArrayList<>());
    }
}
