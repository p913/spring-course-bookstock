package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertNotNull;

@DataMongoTest
class AuthorRepositoryTest {
    @Test
    public void documentMappingTest(@Autowired AuthorRepository authorRepository) {
        Author author = new Author("Author1");

        Mono<Author> authorMono = authorRepository.save(author);

        StepVerifier
                .create(authorMono)
                .assertNext(a -> assertNotNull(a.getId()))
                .expectComplete()
                .verify();

        StepVerifier
                .create(authorRepository.findById(author.getId()))
                .expectNext(author)
                .verifyComplete();
    }

}