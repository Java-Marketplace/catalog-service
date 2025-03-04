package com.jmp.template.repository;

import com.jmp.template.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull();

    boolean existsByNameAndParent(String name, Category parent);

    boolean existsByNameAndParentIsNull(String name);

}
