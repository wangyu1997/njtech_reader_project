package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoriesByName(String name);
    Category findCategoriesById(Long id);
}
