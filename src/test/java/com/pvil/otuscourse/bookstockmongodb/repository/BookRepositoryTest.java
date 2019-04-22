package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void documentMappingAndGenSeqTest() {
        Author author = new Author("Author");
        authorRepository.save(author);

        Book book = new Book("Title", "ISBN", "Publisher", 2000, author, "genre");
        bookRepository.save(book);

        Commentary commentary = new Commentary("Text", new Date(), "Reader");
        bookRepository.addComment(book, commentary);
        book.getComments().add(commentary);

        assertThat(book.getId()).isNotZero();
        Optional<Book> founded = bookRepository.findById(book.getId());
        assertThat(founded.isPresent()).isTrue();
        assertThat(founded.get()).isEqualTo(book);
    }

    @Test
    public void existsByAuthorTest() {
        Author author = new Author("Author2");
        authorRepository.save(author);

        Book book = new Book("Title2", "ISBN2", "Publisher2", 2000, author, "genre2");
        bookRepository.save(book);

        assertThat(bookRepository.existsByAuthor(author)).isTrue();
    }

    @Test
    public void findByAuthorContainsTest() {
        Author author = new Author("Author3");
        authorRepository.save(author);

        Book book = new Book("Title3", "ISBN3", "Publisher3", 2000, author, "genre3");
        bookRepository.save(book);

        assertThat(bookRepository.findByAuthorContains("th")).contains(book);
    }

}