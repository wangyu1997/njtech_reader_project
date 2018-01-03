package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoriesByName(String name);
    Category findCategoriesById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Category b where b.id = ?1")
    int deleteBookById(Long id);
}
