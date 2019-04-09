package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.repository.etalondata.EtalonBooksForTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class BookRepositoryTest {

    @TestConfiguration
    public static class BookRepositoryTestConfig {
        @Bean
        public BookRepository bookRepository() {
            return new BookRepositoryJpa();
        }
    }

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Добавление книги")
    public void addBookTest() {
        Book expected = EtalonBooksForTests.getNonExistent();
        Book book = new Book(expected.getName(), expected.getIsbn(), expected.getPublisher(), expected.getYear(),
                expected.getAuthor(), expected.getGenre());

        bookRepository.add(book);
        assertThat(book.getId()).isEqualTo(expected.getId());

        List<Book> books = bookRepository.getByName(book.getName());
        assertThat(books).containsOnly(book);
    }

    @Test
    @DisplayName("Повторное добавление книги должно вызывать исключение")
    public void addDuplicatedBookTest() {
        assertThatThrownBy( () -> bookRepository.add(EtalonBooksForTests.getExistent()));
    }

    @Test
    @DisplayName("Получение книги по id")
    public void getBookByIdTest() {
        Book existent = EtalonBooksForTests.getExistent();
        Optional<Book> book = bookRepository.getById(existent.getId());

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение книги по имени")
    public void getBookByNameTest() {
        Book existent = EtalonBooksForTests.getExistent();
        List<Book> books = bookRepository.getByName(existent.getName());

        assertThat(books).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение книги по части имени ")
    public void getBookByPartOfNameTest() {
        String partName = "n";
        List<Book> books = bookRepository.getByName(partName);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение книги")
    public void updateBookTest() {
        Book forUpdate = EtalonBooksForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = bookRepository.update(forUpdate);
        assertThat(res).isTrue();

        Optional<Book> updated = bookRepository.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей книги")
    public void updateNonExistingBookTest() {
        boolean res = bookRepository.update(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления книги")
    public void deleteBookTest() {
        Book withoutReferences = EtalonBooksForTests.getWithoutReferences();

        boolean res = bookRepository.delete(withoutReferences);
        assertThat(res).isTrue();

        Optional<Book> deleted = bookRepository.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего книги")
    public void deleteNonExistingBookTest() {
        boolean res = bookRepository.delete(EtalonBooksForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления книги, на которую ссылаются комментарии")
    public void deleteReferencedBookTest() {
        assertThatThrownBy(() -> bookRepository.delete(EtalonBooksForTests.getWithReferences()));
    }

    @Test
    @DisplayName("Выборки всех книг из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Book> books = bookRepository.getAll();

        assertThat(books).containsAll(EtalonBooksForTests.getAll());
    }

    @Test
    @DisplayName("Получение книг по году издания")
    public void getBookByYearTest() {
        int year = EtalonBooksForTests.getAll().get(0).getYear();
        List<Book> books = bookRepository.getByYear(year);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по ISBN")
    public void getBookByIsbnTest() {
        String isbn = "978";
        List<Book> books = bookRepository.getByIsbn(isbn);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(b -> b.getIsbn().contains(isbn)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getBookByAuthorTest() {
        String filter = "n";
        List<Book> books = bookRepository.getByAuthor(filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getBookByGenreTest() {
        String filter = "e";
        List<Book> books = bookRepository.getByGenre(filter);

        List<Book> expected = EtalonBooksForTests.getAll().stream().filter(
                b -> b.getGenre().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

}
