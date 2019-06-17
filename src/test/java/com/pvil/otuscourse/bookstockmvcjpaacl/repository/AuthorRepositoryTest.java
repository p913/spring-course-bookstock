package com.pvil.otuscourse.bookstockmvcjpaacl.repository;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void mappingEntityTest() {
        Author author = new Author("Author");
        authorRepository.save(author);
        assertThat(author.getId()).isNotZero();

        Optional<Author> finded = authorRepository.findById(author.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(author);
    }
}