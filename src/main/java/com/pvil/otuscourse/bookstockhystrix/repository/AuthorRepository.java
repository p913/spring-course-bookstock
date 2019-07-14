package com.pvil.otuscourse.bookstockhystrix.repository;

import com.pvil.otuscourse.bookstockhystrix.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {
    @Override
    List<Author> findAll();

    List<Author> findByName(String name);

    List<Author> findByNameContaining(String name);
}
