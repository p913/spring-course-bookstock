package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;
import com.pvil.otuscourse.bookstockdatajpa.repository.GenreRepository;
import com.pvil.otuscourse.bookstockdatajpa.utils.EntityResolver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenresServiceImpl implements GenresService {
    private final GenreRepository genreRepository;

    public GenresServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> getGenreByName(String filter) {
        return genreRepository.findByNameContaining(filter);
    }

    @Override
    public void addGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public boolean deleteGenre(Genre genre) {
        try {
            genreRepository.deleteById(genre.getId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateGenre(Genre genre) {
        if (!genreRepository.existsById(genre.getId()))
            return false;

        genreRepository.save(genre);
        return true;
    }

    @Override
    public Genre resolveGenre(Genre genre) {
        return (Genre) EntityResolver.resolve(
                new EntityResolver.ResolvingEntityWrapper() {

                    @Override
                    public long getId() {
                        return genre.getId();
                    }

                    @Override
                    public boolean hasIdOnly() {
                        return genre.getName() == null || genre.getName().isEmpty();
                    }

                    @Override
                    public boolean isSame(Object to) {
                        return getEntity().equals(to);
                    }

                    @Override
                    public Object getEntity() {
                        return genre;
                    }
                },
                new EntityResolver.ResolvingEntityDao() {

                    @Override
                    public Optional getById(long id) {
                        return genreRepository.findById(id);
                    }

                    @Override
                    public List getBySample(EntityResolver.ResolvingEntityWrapper entity) {
                        return genreRepository.findByName(((Genre)entity.getEntity()).getName());
                    }
                }

        );
    }

}
