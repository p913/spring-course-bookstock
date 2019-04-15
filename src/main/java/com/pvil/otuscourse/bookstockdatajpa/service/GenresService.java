package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresService {
    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(long id);

    List<Genre> getGenreByName(String filter);

    void addGenre(Genre genre);

    boolean deleteGenre(Genre genre);

    boolean updateGenre(Genre genre);

    Genre resolveGenre(Genre genre);
}
