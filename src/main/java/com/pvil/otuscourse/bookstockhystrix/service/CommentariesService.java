package com.pvil.otuscourse.bookstockhystrix.service;

import com.pvil.otuscourse.bookstockhystrix.domain.Book;
import com.pvil.otuscourse.bookstockhystrix.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void add(Book book, Commentary commentary);

    boolean delete(Book book, Commentary commentary);
}
