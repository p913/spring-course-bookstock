package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;

import java.util.List;

public interface CommentaryRepository {
    List<Commentary> getAllForBook(Book book);

    void add(Commentary commentary);

    boolean delete(Commentary commentary);
}
