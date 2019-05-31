package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentariesService {
    Flux<Commentary> getAllForBook(Book book);

    Mono<Commentary> add(Book book, Commentary commentary);

    Mono<Boolean> delete(Book book, Commentary commentary);
}
