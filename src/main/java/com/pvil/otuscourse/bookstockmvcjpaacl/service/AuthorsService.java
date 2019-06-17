package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
    List<Author> getAllAuthors();

    Optional<Author> getAuthorById(long id);

    List<Author> getAuthorByName(String filter);

    void addAuthor(Author author);

    boolean deleteAuthor(Author author);

    boolean updateAuthor(Author author);

    Author getExistingElseCreate(Author author);
}
