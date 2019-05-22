package com.pvil.otuscourse.bookstockrestajax.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockrestajax.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        bookStockService.save(book);
        assertThat(book.getId()).isNotNull();

        List<Book> books = bookStockService.getByTitle(book.getTitle());
        assertThat(books).containsOnly(book);
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
        List<Book> books = bookStockService.getByTitle(existent.getTitle());

        assertThat(books).containsOnly(existent);
    }

    @Test
    @DisplayName("Изменение книги")
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setTitle("New name");

        bookStockService.save(forUpdate);

        Optional<Book> updated = bookStockService.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Удаления книги")
    public void deleteBookTest() {
        Book candidate = EtalonBooksForTests.getCanBeDeleted();

        bookStockService.delete(candidate);

        Optional<Book> deleted = bookStockService.getById(candidate.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Book> books = bookStockService.getAll();

        assertThat(books).containsAll(EtalonBooksForTests.getAll());
    }

    @Test
    @DisplayName("Поиск книг по части наименования")
    public void getByPartOfTitleTest() {
        String partName = getPartOf(EtalonBooksForTests.getExistent().getTitle());
        List<Book> books = bookStockService.getContainsAnyOf(partName);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(a -> a.getTitle().contains(partName)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getByYearTest() {
        int year = EtalonBooksForTests.getExistent().getYear();
        List<Book> books = bookStockService.getContainsAnyOf(String.valueOf(year));

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по части ISBN")
    public void getByIsbnTest() {
        String isbn = getPartOf(EtalonBooksForTests.getExistent().getIsbn());
        List<Book> books = bookStockService.getContainsAnyOf(isbn);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getIsbn().contains(isbn)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getByAuthorTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getAuthor().getName());
        List<Book> books = bookStockService.getContainsAnyOf(filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getByGenreTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getGenre());
        List<Book> books = bookStockService.getContainsAnyOf(filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getGenre().contains(filter)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    private String getPartOf(String value) {
        return (value.length() > 4) ? value.substring(2, value.length() - 2) : value;
    }

}