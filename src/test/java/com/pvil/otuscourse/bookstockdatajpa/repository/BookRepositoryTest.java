package com.pvil.otuscourse.bookstockdatajpa.repository;

import com.pvil.otuscourse.bookstockdatajpa.domain.Author;
import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void mappingEntityTest() {
        Optional<Author> author = authorRepository.findById(1L);
        assertThat(author.isPresent()).isTrue();

        Optional<Genre> genre = genreRepository.findById(1L);
        assertThat(genre.isPresent()).isTrue();

        Book book = new Book("Title", "ISBN", "Publisher", 2000, author.get(), genre.get());
        bookRepository.save(book);
        assertThat(book.getId()).isNotZero();

        Optional<Book> finded = bookRepository.findById(book.getId());
        assertThat(finded.isPresent()).isTrue();
        assertThat(finded.get()).isEqualTo(book);
    }

}