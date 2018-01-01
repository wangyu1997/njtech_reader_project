package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByTitle(String title);

    Book findBookById(Long id);
}
