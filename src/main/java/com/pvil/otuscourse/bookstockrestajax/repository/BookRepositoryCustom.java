package com.pvil.otuscourse.bookstockrestajax.repository;

import com.pvil.otuscourse.bookstockrestajax.domain.Author;
import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.domain.Commentary;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByAnythingContainsExcludeComments(String filter);

    boolean existsByAuthor(Author author);

    void addComment(Book book, Commentary commentary);

    boolean deleteComment(Book book, Commentary commentary);
}
