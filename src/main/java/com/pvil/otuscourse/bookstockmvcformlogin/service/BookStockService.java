package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    List<Book> getAll();

    List<Book> getByTitle(String title);

    List<Book> getContainsAnyOf(String searchText);

    Optional<Book> getById(String id);

    void save(Book book);

    void delete(Book book);
}
