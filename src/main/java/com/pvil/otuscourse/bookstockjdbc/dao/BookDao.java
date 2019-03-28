package com.pvil.otuscourse.bookstockjdbc.dao;

import com.pvil.otuscourse.bookstockjdbc.domain.Book;

import java.util.List;

public interface BookDao extends NamedEntityWithIdDao<Book> {
    List<Book> getByGenre(String nameFilter);

    List<Book> getByAuthor(String authorFilter);

    List<Book> getByIsbn(String isbnFilter);

    List<Book> getByYear(int year);
}
