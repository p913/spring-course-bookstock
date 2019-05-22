package com.pvil.otuscourse.bookstockrestajax.service;

import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void add(Book book, Commentary commentary);

    boolean delete(Book book, Commentary commentary);
}
