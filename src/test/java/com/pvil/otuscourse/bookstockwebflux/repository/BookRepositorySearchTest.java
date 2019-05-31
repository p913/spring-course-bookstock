package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookRepositorySearchTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final Author author = new Author("Author3");
    private static final Book book =
            new Book("Title3", "ISBN3", "Publisher3", 2000, author, "genre3");

    @BeforeAll
    public void saveBook() {
        authorRepository.save(author).block();

        bookRepository.save(book).block();
    }

    @ParameterizedTest
    @MethodSource("filterForBookSearch")
    public void findByAnythingContainsExcludeCommentsTest(String filter) {

        StepVerifier
                .create(bookRepository.findByAnythingContainsExcludeComments(filter))
                .expectNext(book)
                .verifyComplete();
    }

    static Stream<Arguments> filterForBookSearch() {
        return Stream.of(
                Arguments.of("uthor"), //По совпадению автора
                Arguments.of("itle"),  //По совпадению названия
                Arguments.of("SBN"),   //По совпадению isbn
                Arguments.of("enre"),  //По совпадению жанра
                Arguments.of("ublisher"),  //По совпадению издательства
                Arguments.of("2000")   //По совпадению года издания
        );
    }
}
