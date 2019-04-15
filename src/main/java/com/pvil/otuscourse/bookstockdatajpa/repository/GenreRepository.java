package com.pvil.otuscourse.bookstockdatajpa.repository;

import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    @Override
    List<Genre> findAll();

    List<Genre> findByName(String name);

    List<Genre> findByNameContaining(String name);
}
