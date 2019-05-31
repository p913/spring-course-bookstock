package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookStockService {
    Flux<Book> getAll();

    Flux<Book> getByTitle(String title);

    Flux<Book> getContainsAnyOf(String searchText);

    Mono<Book> getById(String id);

    Mono<Book> save(Book book);

    Mono<Void> delete(Book book);
}
