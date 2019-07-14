package com.pvil.otuscourse.bookstockhystrix.repository;

import com.pvil.otuscourse.bookstockhystrix.domain.Author;
import com.pvil.otuscourse.bookstockhystrix.domain.Book;
import com.pvil.otuscourse.bookstockhystrix.domain.Commentary;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByAnythingContainsExcludeComments(String filter);

    boolean existsByAuthor(Author author);

    void addComment(Book book, Commentary commentary);

    boolean deleteComment(Book book, Commentary commentary);
}
