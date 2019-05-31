package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void documentMappingTest() {
        Author author = new Author("Author");
        Book book = new Book("Title", "ISBN", "Publisher", 2000, author, "genre");
        Commentary commentary = new Commentary("Text", new Date(), "Reader");

        authorRepository.save(author).block();

        StepVerifier
                .create(bookRepository.save(book))
                .assertNext(b -> assertThat(b.getId()).isNotNull())
                .verifyComplete();

        StepVerifier
                .create(bookRepository.addComment(book, commentary))
                .assertNext(c -> assertThat(c.getId()).isNotNull())
                .verifyComplete();

        book.getComments().add(commentary);

        StepVerifier
                .create(bookRepository.findById(book.getId()))
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    public void existsByAuthorTest() {
        Author author = new Author("Author2");
        Book book = new Book("Title2", "ISBN2", "Publisher2", 2000, author, "genre2");

        authorRepository.save(author).block();

        bookRepository.save(book).block();

        StepVerifier
                .create(bookRepository.existsByAuthor(author))
                .assertNext(r -> assertThat(r).isTrue())
                .verifyComplete();
    }

}