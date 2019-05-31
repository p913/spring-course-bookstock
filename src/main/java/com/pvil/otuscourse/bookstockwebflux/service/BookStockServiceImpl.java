package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookRepository bookRepository;

    private final AuthorsService authorsService;

    public BookStockServiceImpl(BookRepository bookRepository, AuthorsService authorsService) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
    }

    @Override
    public Flux<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Flux<Book> getByTitle(String title) {
        return bookRepository.findByTitleExcludeComments(title);
    }

    @Override
    public Flux<Book> getContainsAnyOf(String searchText) {
        return bookRepository.findByAnythingContainsExcludeComments(searchText);
    }

    @Override
    public Mono<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Mono<Book> save(Book book) {
        Book b2 = book;
        return authorsService.getExistingElseCreate(book.getAuthor()).flatMap(a -> {
            book.setAuthor(a);
            return bookRepository.save(book);
        });
    }

    @Override
    public Mono<Void> delete(Book book) {
        return bookRepository.deleteById(book.getId()).then();
    }


}
