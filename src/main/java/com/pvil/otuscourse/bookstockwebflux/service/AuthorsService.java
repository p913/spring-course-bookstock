package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorsService {
    Flux<Author> getAll();

    Mono<Author> getById(String id);

    Flux<Author> getByPartName(String filter);

    Flux<Author> getByName(String name);

    Mono<Author> save(Author author);

    Mono<Void> delete(Author author);

    Mono<Author> getExistingElseCreate(Author author);
}
