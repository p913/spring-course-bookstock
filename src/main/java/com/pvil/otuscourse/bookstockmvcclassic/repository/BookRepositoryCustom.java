package com.pvil.otuscourse.bookstockmvcclassic.repository;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Author;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByAnythingContainsExcludeComments(String filter);

    boolean existsByAuthor(Author author);

    void addComment(Book book, Commentary commentary);

    boolean deleteComment(Book book, Commentary commentary);
}
