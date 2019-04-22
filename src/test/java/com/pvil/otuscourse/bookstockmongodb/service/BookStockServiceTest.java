package com.pvil.otuscourse.bookstockmongodb.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.etalondata.EtalonBooksForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
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
        Book expected = EtalonBooksForTests.getCanBeAdded();
        Book book = new Book(expected.getTitle(), expected.getIsbn(), expected.getPublisher(), expected.getYear(),
                expected.getAuthor(), expected.getGenre());

        bookStockService.add(book);
        assertThat(book.getId()).isEqualTo(expected.getId());

        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.TITLE, book.getTitle());
        assertThat(books).containsOnly(book);
    }

    @Test
    @DisplayName("Повторное добавление книги должно вызывать исключение")
    public void addDuplicatedBookTest() {
        Book book = new Book(EtalonBooksForTests.getExistent().getTitle(),
                EtalonBooksForTests.getExistent().getIsbn(),
                EtalonBooksForTests.getExistent().getPublisher(),
                EtalonBooksForTests.getExistent().getYear(),
                EtalonBooksForTests.getExistent().getAuthor(),
                EtalonBooksForTests.getExistent().getGenre());
        assertThatThrownBy( () -> bookStockService.add(book));
    }

    @Test
    @DisplayName("Получение книги по id")
    public void getByIdTest() {
        Book existent = EtalonBooksForTests.getExistent();
        Optional<Book> book = bookStockService.getById(existent.getId());

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение книги по имени")
    public void getByNameTest() {
        Book existent = EtalonBooksForTests.getExistent();
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.TITLE, existent.getTitle());

        assertThat(books).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение книги по части имени ")
    public void getByPartOfNameTest() {
        String partName = "n";
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.TITLE, partName);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(a -> a.getTitle().contains(partName)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение книги")
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setTitle("New name");

        boolean res = bookStockService.update(forUpdate);
        assertThat(res).isTrue();

        Optional<Book> updated = bookStockService.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей книги")
    public void updateNonExistingBookTest() {
        boolean res = bookStockService.update(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления книги")
    public void deleteBookTest() {
        Book candidate = EtalonBooksForTests.getCanBeDeleted();

        boolean res = bookStockService.delete(candidate);
        assertThat(res).isTrue();

        Optional<Book> deleted = bookStockService.getById(candidate.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующей книги")
    public void deleteNonExistingBookTest() {
        boolean res = bookStockService.delete(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Book> books = bookStockService.getAll();

        assertThat(books).containsAll(EtalonBooksForTests.getAll());
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getByYearTest() {
        int year = EtalonBooksForTests.getAll().get(0).getYear();
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.YEAR, String.valueOf(year));

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по ISBN")
    public void getByIsbnTest() {
        String isbn = "978";
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.ISBN, isbn);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getIsbn().contains(isbn)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getByAuthorTest() {
        String filter = "n";
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.AUTHOR, filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getByGenreTest() {
        String filter = "e";
        List<Book> books = bookStockService.getByCriterion(BookSearchCriterion.GENRE, filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getGenre().contains(filter)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

}