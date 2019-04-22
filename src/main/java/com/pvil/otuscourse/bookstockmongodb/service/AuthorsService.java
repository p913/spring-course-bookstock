package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
    List<Author> getAll();

    Optional<Author> getById(long id);

    List<Author> getByPartName(String filter);

    List<Author> getByName(String name);

    void add(Author author);

    boolean delete(Author author);

    boolean update(Author author);

    Author getExistingElseCreate(Author author);
}
