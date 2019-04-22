package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    List<Book> getAll();

    Optional<Book> getByTitle(String title);

    List<Book> getByCriterion(BookSearchCriterion criterion, String filter);

    Optional<Book> getById(long id);

    void add(Book book);

    boolean delete(Book book);

    boolean update(Book book);
}
