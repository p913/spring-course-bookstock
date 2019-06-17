package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Genre;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.GenreRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Genre getExistingElseCreate(Genre genre) {
        if (genre.getId() != 0 ) {
            Optional<Genre> founded = genreRepository.findById(genre.getId());
            if (founded.isEmpty())
                throw new EntityNotFoundException(String.format("Genre with Id = %s not found", genre.getId()));
            else if (genre.getName() == null || genre.getName().isEmpty())
                return founded.get();
            else if (!genre.equals(founded.get()))
                throw new EntityNotFoundException(String.format("Genre with Id = %s is different genre than %s (%s)",
                        genre.getId(), genre.toString(), founded.get().toString()));
            else
                return founded.get();
        } else {
            List<Genre> entities = genreRepository.findByName(genre.getName());
            if (entities.isEmpty())
                return genreRepository.save(genre);
            else
                return entities.get(0);
        }
    }

}
