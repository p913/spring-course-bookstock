package com.pvil.otuscourse.bookstockmvcclassic.service;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void add(Book book, Commentary commentary);

    boolean delete(Book book, Commentary commentary);
}
