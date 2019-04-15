package com.pvil.otuscourse.bookstockdatajpa.repository;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Override
    @Query("select b from Book b join fetch b.author join fetch b.genre")
    List<Book> findAll();

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.name like %:filter%")
    List<Book> findByName(String filter);

    @Query("select b from Book b join fetch b.author join fetch b.genre g where g.name like %:filter%")
    List<Book> findByGenre(String filter);

    @Query("select b from Book b join fetch b.author a join fetch b.genre where a.name like %:filter%")
    List<Book> findByAuthor(String filter);

    @Query("select b from Book b join fetch b.author join fetch b.genre g where b.isbn like %:filter%")
    List<Book> findByIsbn(String filter);

    @Query("select b from Book b join fetch b.author join fetch b.genre g where b.year = :year")
    List<Book> findByYear(int year);
}
