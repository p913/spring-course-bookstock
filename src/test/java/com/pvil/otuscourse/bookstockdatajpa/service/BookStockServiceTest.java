package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.etalondata.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockdatajpa.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockdatajpa.repository.BookRepository;
import com.pvil.otuscourse.bookstockdatajpa.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookStockServiceTest {
    @TestConfiguration
    public static class BookStockServiceTestConfiguration {

        @Bean
        public GenresService getGenresService(GenreRepository genreRepository) {
            return new GenresServiceImpl(genreRepository);
        }

        @Bean
        public AuthorsService getAuthorsService(AuthorRepository authorRepository) {
            return new AuthorsServiceImpl(authorRepository);
        }

        @Bean
        public BookStockService getBookStockService(BookRepository bookRepository,
                                                    AuthorsService authorsService, GenresService genresService) {
            return new BookStockServiceImpl(bookRepository, authorsService, genresService);
        }
    }

    @Autowired
    private BookStockService bookStockService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Добавление книги")
    public void addBookTest() {
        Book expected = EtalonBooksForTests.getNonExistent();
        Book book = new Book(expected.getName(), expected.getIsbn(), expected.getPublisher(), expected.getYear(),
                expected.getAuthor(), expected.getGenre());

        bookStockService.addBook(book);
        assertThat(book.getId()).isEqualTo(expected.getId());

        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.NAME, book.getName());
        assertThat(books).containsOnly(book);
    }

    @Test
    @DisplayName("Повторное добавление книги должно вызывать исключение")
    public void addDuplicatedBookTest() {
        Book book = new Book(EtalonBooksForTests.getExistent().getName(),
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
    @DisplayName("Получение книги по имени")
    public void getBookByNameTest() {
        Book existent = EtalonBooksForTests.getExistent();
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.NAME, existent.getName());

        assertThat(books).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение книги по части имени ")
    public void getBookByPartOfNameTest() {
        String partName = "n";
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.NAME, partName);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение книги")
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = bookStockService.updateBook(forUpdate);
        assertThat(res).isTrue();

        Optional<Book> updated = bookStockService.getBookById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей книги")
    public void updateNonExistingBookTest() {
        boolean res = bookStockService.updateBook(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления книги")
    public void deleteBookTest() {
        Book withoutReferences = EtalonBooksForTests.getWithoutReferences();

        boolean res = bookStockService.deleteBook(withoutReferences);
        assertThat(res).isTrue();

        Optional<Book> deleted = bookStockService.getBookById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующей книги")
    public void deleteNonExistingBookTest() {
        boolean res = bookStockService.deleteBook(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления книги, на которую ссылаются комментарии")
    public void deleteReferencedBookTest() {
        assertThatThrownBy(() -> {
                bookStockService.deleteBook(EtalonBooksForTests.getWithReferences());
                testEntityManager.flush(); //аналгично авторам - Hibernate выполнит delete
        } );
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Book> books = bookStockService.getAllBooks();

        assertThat(books).containsAll(EtalonBooksForTests.getAll());
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getBookByYearTest() {
        int year = EtalonBooksForTests.getAll().get(0).getYear();
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.YEAR, String.valueOf(year));

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по ISBN")
    public void getBookByIsbnTest() {
        String isbn = "978";
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.ISBN, isbn);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getIsbn().contains(isbn)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getBookByAuthorTest() {
        String filter = "n";
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.AUTHOR, filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getBookByGenreTest() {
        String filter = "e";
        List<Book> books = bookStockService.getBookByCriterion(BookSearchCriterion.GENRE, filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getGenre().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }
}