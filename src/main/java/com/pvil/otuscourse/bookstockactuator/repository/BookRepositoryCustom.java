package com.pvil.otuscourse.bookstockactuator.repository;

import com.pvil.otuscourse.bookstockactuator.domain.Author;
import com.pvil.otuscourse.bookstockactuator.domain.Book;
import com.pvil.otuscourse.bookstockactuator.domain.Commentary;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByAnythingContainsExcludeComments(String filter);

    boolean existsByAuthor(Author author);

    void addComment(Book book, Commentary commentary);

    boolean deleteComment(Book book, Commentary commentary);
}
