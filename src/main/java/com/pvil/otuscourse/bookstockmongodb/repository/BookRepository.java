package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * В репозитарии методы {@code findBy...} не возвращают комментарии, т.к. их может быть много.
 * Для получения книги вместе с комментариями использовать {@code findById()}
 */
public interface BookRepository extends MongoRepository<Book, Long>, BookRepositoryCustom {
    @Override
    @Query(value="{}", fields="{comments: 0}")
    List<Book> findAll();

    @Query(fields="{comments: 0}")
    List<Book> findByTitle(String title);

    @Query(fields="{comments: 0}")
    List<Book> findByTitleContains(String filter);

    @Query(fields="{comments: 0}")
    List<Book> findByGenreContains(String filter);

    @Query(fields="{comments: 0}")
    List<Book> findByIsbnContains(String filter);

    @Query(fields="{comments: 0}")
    List<Book> findByYear(int year);

    @Override
    void addComment(Book book, Commentary commentary);

    @Override
    boolean deleteComment(Book book, Commentary commentary);
}
