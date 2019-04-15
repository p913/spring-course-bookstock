package com.pvil.otuscourse.bookstockdatajpa.repository;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentaryRepositoryTest {
    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void mappingEntityTest() {
        Optional<Book> book = bookRepository.findById(1L);
        assertThat(book.isPresent()).isTrue();

        Commentary comment = new Commentary("Comment", OffsetDateTime.now(), "Reader", book.get());
        commentaryRepository.save(comment);
        assertThat(comment.getId()).isNotZero();

        Optional<Commentary> finded = commentaryRepository.findById(comment.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(comment);
    }
}