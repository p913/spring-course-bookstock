package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresService {
    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(int id);

    List<Genre> getGenreByName(String filter);

    int addGenre(Genre genre);

    boolean deleteGenre(Genre genre);

    boolean updateGenre(Genre genre);

    Genre resolveGenre(Genre genre);
}
