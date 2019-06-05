package com.pvil.otuscourse.bookstockmvcformlogin.repository;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Author;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByAnythingContainsExcludeComments(String filter);

    boolean existsByAuthor(Author author);

    void addComment(Book book, Commentary commentary);

    boolean deleteComment(Book book, Commentary commentary);
}
