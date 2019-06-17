package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.etalondata.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockPredefinedAuthorities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@WithMockUser(username = "reader", authorities = {BookStockPredefinedAuthorities.ROLE_READER})
@Transactional
class BookStockServiceTest {

    @Autowired
    private BookStockService bookStockService;

    @Autowired
    private EntityManager testEntityManager;

    @Test
    @DisplayName("Добавление книги")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void addBookTest() {
        Book expected = EtalonBooksForTests.getCanBeAdded();
        Book book = new Book(expected.getTitle(), expected.getIsbn(), expected.getPublisher(), expected.getYear(),
                expected.getAuthor(), expected.getGenre());

        bookStockService.addBook(book);
        assertThat(book.getId()).isNotZero();
        expected.setId(book.getId());

        List<Book> books = bookStockService.searchBooks(book.getTitle());
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
        assertThatThrownBy( () -> bookStockService.addBook(book));
    }

    @Test
    @DisplayName("Получение книги по id")
    public void getBookByIdTest() {
        Book existent = EtalonBooksForTests.getExistent();
        Optional<Book> book = bookStockService.getBookById(existent.getId());

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Изменение книги")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setTitle("New name");

        boolean res = bookStockService.updateBook(forUpdate);
        assertThat(res).isTrue();

        Optional<Book> updated = bookStockService.getBookById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Удаления книги")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void deleteBookTest() {
        Book withoutReferences = EtalonBooksForTests.getCanBeDeleted();

        boolean res = bookStockService.deleteBook(withoutReferences);
        assertThat(res).isTrue();

        Optional<Book> deleted = bookStockService.getBookById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления книги, на которую ссылаются комментарии")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void deleteReferencedBookTest() {
        Book withComments = EtalonBooksForTests.getWithComments();

        boolean res = bookStockService.deleteBook(withComments);
        assertThat(res).isTrue();

        Optional<Book> deleted = bookStockService.getBookById(withComments.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Book> books = bookStockService.getAllBooks();

        assertThat(books).containsAll(EtalonBooksForTests.getAll());
    }

    @Test
    @DisplayName("Получение книги по части имени ")
    public void getBookByPartOfNameTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getIsbn());
        List<Book> books = bookStockService.searchBooks(filter);

        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(a -> a.getTitle().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getBookByYearTest() {
        int year = EtalonBooksForTests.getAll().get(0).getYear();
        List<Book> books = bookStockService.searchBooks(String.valueOf(year));

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по ISBN")
    public void getBookByIsbnTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getIsbn());
        List<Book> books = bookStockService.searchBooks(filter);

        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getIsbn().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getBookByAuthorTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getAuthor().getName());
        List<Book> books = bookStockService.searchBooks(filter);

        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream().filter(b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getBookByGenreTest() {
        String filter = getPartOf(EtalonBooksForTests.getExistent().getGenre().getName());
        List<Book> books = bookStockService.searchBooks(filter);

        List<Book> expected = EtalonBooksForTests
                .getAll()
                .stream()
                .filter(b -> b.getGenre().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    private String getPartOf(String value) {
        return (value.length() > 4) ? value.substring(2, value.length() - 2) : value;
    }


}