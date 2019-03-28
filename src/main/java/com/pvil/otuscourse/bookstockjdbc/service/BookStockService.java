package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
import com.pvil.otuscourse.bookstockjdbc.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    List<Book> getAllBooks();

    List<Book> getBookByName(String nameFilter);

    List<Book> getBookByGenre(String genreFilter);

    List<Book> getBookByAuthor(String authorFilter);

    List<Book> getBookByIsbn(String isbnFilter);

    List<Book> getBookByYear(int year);

    Optional<Book> getBookById(int id);

    int addBook(Book book);

    boolean deleteBook(Book book);

    boolean updateBook(Book book);
}
