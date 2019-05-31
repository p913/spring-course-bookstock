package com.pvil.otuscourse.bookstockwebflux.service;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockwebflux.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public AuthorsServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Flux<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> getById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public Flux<Author> getByPartName(String filter) {
        return authorRepository.findByNameContaining(filter);
    }

    @Override
    public Flux<Author> getByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public Mono<Author> save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Mono<Void> delete(Author author) {
        return bookRepository.existsByAuthor(author).flatMap(b ->
            b ? Mono.error(new EntityHasReferences(String.format("There are book that write author with Id = %s", author.getId())))
                : authorRepository.deleteById(author.getId())
        ).then();
    }

    @Override
    public Mono<Author> getExistingElseCreate(Author author) {
        if (author.getId() != null) {
            return authorRepository
                    .findById(author.getId())
                    .flatMap(a ->
                        (author.getName() != null && !author.getName().isEmpty() && !author.equals(a))
                                ? Mono.error(new EntityNotFoundException(String.format("Author with Id = %s is different author than %s (%s)",
                                    author.getId(), author.toString(), a.toString())))
                                : Mono.just(a)
                    )
                    .switchIfEmpty(Mono.error(new EntityNotFoundException(String.format("Author with Id = %s not found", author.getId()))));
        } else
            return authorRepository
                    .findByName(author.getName())
                    .next()
                    .switchIfEmpty(authorRepository.save(author));
    }
}
