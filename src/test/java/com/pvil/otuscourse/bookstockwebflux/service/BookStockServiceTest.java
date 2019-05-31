package com.pvil.otuscourse.bookstockwebflux.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockwebflux.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookStockServiceTest {
    @Autowired
    private BookStockService bookStockService;

    @Autowired
    private SpringMongock mongock;

    @BeforeEach
    public void prepareDatabase() {
        mongock.execute();
    }

    @Test
    @DisplayName("Добавление книги")
    public void addBookTest() {
        Book book = EtalonBooksForTests.getCanBeAdded();

        StepVerifier
                .create(bookStockService.save(book))
                .assertNext(b -> assertThat(b.getId()).isNotNull())
                .verifyComplete();

        StepVerifier
                .create(bookStockService.getByTitle(book.getTitle()))
                .expectNext(book)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Получение книги по id")
    public void getByIdTest() {
        Book existent = EtalonBooksForTests.getExistent();

        StepVerifier
                .create(bookStockService.getById(existent.getId()))
                .expectNext(existent)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение книги по имени")
    public void getByNameTest() {
        Book existent = EtalonBooksForTests.getExistent();

        StepVerifier
                .create(bookStockService.getByTitle(existent.getTitle()))
                .expectNext(existent)
                .verifyComplete();
    }

    @Test
    @DisplayName("Изменение книги")
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setTitle("New name");

        bookStockService
                .save(forUpdate)
                .block();

        StepVerifier
                .create(bookStockService.getById(forUpdate.getId()))
                .expectNext(forUpdate)
                .verifyComplete();
    }

    @Test
    @DisplayName("Удаления книги")
    public void deleteBookTest() {
        Book candidate = EtalonBooksForTests.getCanBeDeleted();

        bookStockService
                .delete(candidate)
                .block();

        Optional<Book> deleted = bookStockService
                .getById(candidate.getId())
                .blockOptional();
        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        StepVerifier
                .create(bookStockService.getAll())
                .expectNextSequence(EtalonBooksForTests.getAll())
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск книг по части наименования")
    public void getByPartOfTitleTest() {
        String partName = getPartOf(EtalonBooksForTests.getExistent().getTitle());
        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(a -> a.getTitle().contains(partName))
                .collect(Collectors.toList());

        StepVerifier
                .create(bookStockService.getContainsAnyOf(partName))
                .expectNextSequence(expected)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getByYearTest() {
        int year = EtalonBooksForTests.getExistent().getYear();
        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getYear() == year)
                .collect(Collectors.toList());

        StepVerifier
                .create(bookStockService.getContainsAnyOf(String.valueOf(year)))
                .expectNextSequence(expected)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение книг по части ISBN")
    public void getByIsbnTest() {
        String isbn = getPartOf(EtalonBooksForTests.getExistent().getIsbn());
        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getIsbn().contains(isbn))
                .collect(Collectors.toList());

        StepVerifier
                .create(bookStockService.getContainsAnyOf(isbn))
                .expectNextSequence(expected)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getByAuthorTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getAuthor().getName());
        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());

        StepVerifier
                .create(bookStockService.getContainsAnyOf(filter))
                .expectNextSequence(expected)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getByGenreTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getGenre());
        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getGenre().contains(filter))
                .collect(Collectors.toList());

        StepVerifier
                .create(bookStockService.getContainsAnyOf(filter))
                .expectNextSequence(expected)
                .verifyComplete();
    }

    private String getPartOf(String value) {
        return (value.length() > 4) ? value.substring(1, value.length() - 1) : value;
    }

}