package com.pvil.otuscourse.bookstockmvcformlogin.repository;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Author;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.User;
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

    @Autowired
    private UserRepository userRepository;

    @Test
    public void documentMappingTest() {
        Author author = new Author("Author");
        authorRepository.save(author);

        Book book = new Book("Title", "ISBN", "Publisher", 2000, author, "genre");
        bookRepository.save(book);

        User user = new User("Login", "Password", "Full Name", "e@mail.com", true);
        userRepository.save(user);

        Commentary commentary = new Commentary("Text", new Date(), user);
        bookRepository.addComment(book, commentary);
        book.getComments().add(commentary);

        assertThat(book.getId()).isNotNull();
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
    public void findByAnythingContainsExcludeCommentsTest() {
        Author author = new Author("Author3");
        authorRepository.save(author);

        Book book = new Book("Title3", "ISBN3", "Publisher3", 2000, author, "genre3");
        bookRepository.save(book);

        //По совпадению автора
        assertThat(bookRepository.findByAnythingContainsExcludeComments("uthor")).contains(book);
        //По совпадению названия
        assertThat(bookRepository.findByAnythingContainsExcludeComments("itle")).contains(book);
        //По совпадению isbn
        assertThat(bookRepository.findByAnythingContainsExcludeComments("SBN")).contains(book);
        //По совпадению жанра
        assertThat(bookRepository.findByAnythingContainsExcludeComments("enre")).contains(book);
        //По совпадению издательства
        assertThat(bookRepository.findByAnythingContainsExcludeComments("ublisher")).contains(book);
        //По совпадению года издания
        assertThat(bookRepository.findByAnythingContainsExcludeComments("2000")).contains(book);
    }

}