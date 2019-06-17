package com.pvil.otuscourse.bookstockmvcjpaacl.repository;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Override
    List<Author> findAll();

    List<Author> findByName(String name);

    List<Author> findByNameContaining(String name);
}
