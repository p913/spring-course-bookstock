package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> getAll();

    Optional<Author> getById(long id);

    List<Author> getByName(String filter);

    void add(Author author);

    boolean delete(Author author);

    boolean update(Author author);
}
