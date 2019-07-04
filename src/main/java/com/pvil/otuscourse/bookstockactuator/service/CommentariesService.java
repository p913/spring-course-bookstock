package com.pvil.otuscourse.bookstockactuator.service;

import com.pvil.otuscourse.bookstockactuator.domain.Book;
import com.pvil.otuscourse.bookstockactuator.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void add(Book book, Commentary commentary);

    boolean delete(Book book, Commentary commentary);
}
