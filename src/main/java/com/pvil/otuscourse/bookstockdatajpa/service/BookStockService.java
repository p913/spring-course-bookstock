package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    List<Book> getAllBooks();

    List<Book> getBookByCriterion(BookSearchCriterion criterion, String filter);

    Optional<Book> getBookById(long id);

    void addBook(Book book);

    boolean deleteBook(Book book);

    boolean updateBook(Book book);

    Book resolveBook(Book author);

}
