package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    @Override
    Flux<Author> findAll();

    Flux<Author> findByName(String name);

    Flux<Author> findByNameContaining(String name);
}
