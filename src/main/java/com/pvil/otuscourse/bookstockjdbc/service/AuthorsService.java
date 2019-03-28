package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
    List<Author> getAllAuthors();

    Optional<Author> getAuthorById(int id);

    List<Author> getAuthorByName(String filter);

    int addAuthor(Author author);

    boolean deleteAuthor(Author author);

    boolean updateAuthor(Author author);

    Author resolveAuthor(Author author);
}
