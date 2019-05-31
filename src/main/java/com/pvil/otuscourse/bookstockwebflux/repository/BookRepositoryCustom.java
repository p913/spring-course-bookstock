package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepositoryCustom {
    Flux<Book> findByAnythingContainsExcludeComments(String filter);

    Mono<Boolean> existsByAuthor(Author author);

    Mono<Commentary> addComment(Book book, Commentary commentary);

    Mono<Boolean> deleteComment(Book book, Commentary commentary);
}
