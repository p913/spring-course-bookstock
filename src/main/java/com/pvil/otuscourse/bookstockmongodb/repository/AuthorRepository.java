package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, Long> {
    @Override
    List<Author> findAll();

    List<Author> findByName(String name);

    List<Author> findByNameContaining(String name);
}
