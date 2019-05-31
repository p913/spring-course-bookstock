package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {
    @Query(value="{}", fields="{comments: 0}")
    Flux<Book> findAllExcludeComments();

    @Query(value="{title: ?0}", fields="{comments: 0}")
    Flux<Book> findByTitleExcludeComments(String title);
}
