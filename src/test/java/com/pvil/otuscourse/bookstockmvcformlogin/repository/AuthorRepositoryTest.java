package com.pvil.otuscourse.bookstockmvcformlogin.repository;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuthorRepositoryTest {
    @Test
    public void documentMappingTest(@Autowired AuthorRepository authorRepository) {
        Author author = new Author("Author1");

        authorRepository.save(author);

        assertThat(author.getId()).isNotNull();
        Optional<Author> founded = authorRepository.findById(author.getId());
        assertThat(founded.isPresent()).isTrue();
        assertThat(founded.get()).isEqualTo(author);
    }

}