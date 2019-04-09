package com.pvil.otuscourse.bookstockjpajpql.service;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;

import java.util.List;

public interface CommentariesService {
    List<Commentary> getAllForBook(Book book);

    void addCommentary(Commentary commentary);

    boolean deleteCommentary(Commentary commentary);
}
