package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void add(Book book, Commentary commentary);

    boolean delete(Book book, Commentary commentary);
}
