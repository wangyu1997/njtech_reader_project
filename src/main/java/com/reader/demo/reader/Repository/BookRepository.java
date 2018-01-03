package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.Book;
import com.reader.demo.reader.Model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByTitle(String title);

    Book findBookById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Book b where b.id = ?1")
    int deleteBookById(Long id);

    @Query(value = "SELECT count(*) FROM sys_user_book a WHERE a.user_id = ?1 AND a.book_id = ?2", nativeQuery = true)
    int findCollectCountByBookIdAndUserId(Long user_id, Long book_id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO  sys_user_book(user_id,book_id) VALUES(?1,?2)", nativeQuery = true)
    int addCollection(Long user_id, Long book_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sys_user_book WHERE user_id = ?1 AND  book_id = ?2", nativeQuery = true)
    int cancelCollection(Long user_id, Long book_id);

    @Query(value = "SELECT * FROM book WHERE id IN (SELECT book_id FROM sys_user_book a WHERE a.user_id = ?1)", nativeQuery = true)
    List<Book> findAllCollectById(Long user_id);


    @Query(value="select * from book a where a.title like CONCAT('%',:title,'%')",nativeQuery=true)
    List<Book> findLikeBookTitle(@Param("title") String title);
}
