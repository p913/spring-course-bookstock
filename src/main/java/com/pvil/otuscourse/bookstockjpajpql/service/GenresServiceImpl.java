package com.pvil.otuscourse.bookstockjpajpql.service;

import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;
import com.pvil.otuscourse.bookstockjpajpql.repository.GenreRepository;
import com.pvil.otuscourse.bookstockjpajpql.utils.EntityNotFoundException;
import com.pvil.otuscourse.bookstockjpajpql.utils.EntityResolver;
import com.pvil.otuscourse.bookstockjpajpql.utils.TooManyEntitiesException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenresServiceImpl implements GenresService {
    private final GenreRepository genreRepository;

    public GenresServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.getAll();
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        return genreRepository.getById(id);
    }

    @Override
    public List<Genre> getGenreByName(String filter) {
        return genreRepository.getByName(filter);
    }

    @Override
    public void addGenre(Genre genre) {
        genreRepository.add(genre);
    }

    @Override
    public boolean deleteGenre(Genre genre) {
        return genreRepository.delete(genre);
    }

    @Override
    public boolean updateGenre(Genre genre) {
        return genreRepository.update(genre);
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
                        return genreRepository.getById(id);
                    }

                    @Override
                    public List getBySample(EntityResolver.ResolvingEntityWrapper entity) {
                        return genreRepository.getByName(((Genre)entity.getEntity()).getName());
                    }
                }

        );
    }

}
