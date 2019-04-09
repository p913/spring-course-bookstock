package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> getAll();

    Optional<Genre> getById(long id);

    List<Genre> getByName(String filter);

    void add(Genre genre);

    boolean delete(Genre genre);

    boolean update(Genre genre);
    
}
