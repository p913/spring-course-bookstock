package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import com.pvil.otuscourse.bookstockwebflux.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentariesServiceImpl implements CommentariesService {
    private final BookRepository bookRepository;

    public CommentariesServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Flux<Commentary> getAllForBook(Book book) {
        return bookRepository.findById(book.getId()).defaultIfEmpty(new Book()).flatMapMany(b -> Flux.fromIterable(b.getComments()));
    }

    @Override
    public Mono<Commentary> add(Book book, Commentary commentary) {
        return bookRepository.addComment(book, commentary);
    }

    @Override
    public Mono<Boolean> delete(Book book, Commentary commentary) {
        return bookRepository.deleteComment(book, commentary);
    }
}
