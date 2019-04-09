package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> getAll();

    Optional<Book> getById(long id);

    List<Book> getByName(String nameFilter);

    List<Book> getByGenre(String genre);

    List<Book> getByAuthor(String authorFilter);

    List<Book> getByIsbn(String isbnFilter);

    List<Book> getByYear(int year);

    void add(Book book);

    boolean delete(Book book);

    boolean update(Book book);
}
