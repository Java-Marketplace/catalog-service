package com.jmp.catalogservice.repository;

import com.jmp.catalogservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    List<Category> findByParentId(Long parentId);
    List<Category> findByParentIsNull();
}
