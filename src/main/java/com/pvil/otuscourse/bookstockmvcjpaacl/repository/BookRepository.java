package com.pvil.otuscourse.bookstockmvcjpaacl.repository;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    @Query("select b from Book b join fetch b.author join fetch b.genre")
    List<Book> findAll();

    @Query("select b from Book b join fetch b.author a join fetch b.genre g " +
            "where UPPER(b.title) like CONCAT('%', UPPER(:filter), '%') " +
            " or UPPER(b.isbn) like CONCAT('%', UPPER(:filter), '%') " +
            " or UPPER(b.publisher) like CONCAT('%', UPPER(:filter), '%') " +
            " or CONCAT('', b.year) = :filter " +
            " or UPPER(a.name) like CONCAT('%', UPPER(:filter), '%') " +
            " or UPPER(g.name) like CONCAT('%', UPPER(:filter), '%') ")
    List<Book> findByAnythingContains(String filter);

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.title = :title")
    List<Book> findByTitle(String title);

}
