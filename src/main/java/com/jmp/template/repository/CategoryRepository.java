package com.jmp.template.repository;

import com.jmp.template.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name); // Проверка уникальности имени

    List<Category> findByParentId(Long parentId); // Поиск дочерних категорий

}
