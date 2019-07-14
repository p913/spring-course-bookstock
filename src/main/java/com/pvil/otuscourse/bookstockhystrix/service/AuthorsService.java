package com.pvil.otuscourse.bookstockhystrix.service;

import com.pvil.otuscourse.bookstockhystrix.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsService {
    List<Author> getAll();

    Optional<Author> getById(String id);

    List<Author> getByPartName(String filter);

    List<Author> getByName(String name);

    void save(Author author);

    void delete(Author author);

    Author getExistingElseCreate(Author author);
}
