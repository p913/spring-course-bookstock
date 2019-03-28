package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.dao.GenreDao;
import com.pvil.otuscourse.bookstockjdbc.domain.Genre;
import com.pvil.otuscourse.bookstockjdbc.utils.EntityResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService {
    private final GenreDao genreDao;

    public GenresServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Genre> getGenreByName(String filter) {
        return null;
    }

    @Override
    public int addGenre(Genre genre) {
        return 0;
    }

    @Override
    public boolean deleteGenre(Genre genre) {
        return false;
    }

    @Override
    public boolean updateGenre(Genre genre) {
        return false;
    }

    @Override
    public Genre resolveGenre(Genre genre) {
        return EntityResolver.<Genre, GenreDao>resolve(genre, genreDao);
    }

}
