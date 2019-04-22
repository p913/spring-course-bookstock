package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuthorRepositoryTest {
    @Test
    public void documentMappingAndGenSeqTest(@Autowired AuthorRepository authorRepository) {
        Author author = new Author("Author1");

        authorRepository.save(author);

        assertThat(author.getId()).isNotZero();
        Optional<Author> founded = authorRepository.findById(author.getId());
        assertThat(founded.isPresent()).isTrue();
        assertThat(founded.get()).isEqualTo(author);
    }

}