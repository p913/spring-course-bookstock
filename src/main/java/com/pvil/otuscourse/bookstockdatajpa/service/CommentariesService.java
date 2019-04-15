package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void addCommentary(Commentary commentary);

    boolean deleteCommentary(Commentary commentary);
}
