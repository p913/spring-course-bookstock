package com.pvil.otuscourse.bookstockjdbc;

import com.pvil.otuscourse.bookstockjdbc.dao.*;
import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
import com.pvil.otuscourse.bookstockjdbc.domain.NamedEntityWithId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;


@JdbcTest
/**
 * Тесты в AnyDaoTest охватывают книги не полностью, поэтому здесь выполним дополнительные
 */
public class BookDaoExtendedTest {
    @TestConfiguration
    public static class TestJdbcConfiguration {
        @Autowired
        private NamedParameterJdbcOperations jdbc;

        @Bean
        public BookDao getBookDao() {
            return new BookDaoDb(jdbc);
        }
    }

    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName("Получение книг по году издания")
    public void getBookByYearTest() {
        int year = AnyDaoTest.predefinedBooks.get(0).getYear();
        List<Book> books = bookDao.getByYear(year);

        List<Book> expected = AnyDaoTest.predefinedBooks.stream().filter(b -> b.getYear() == year).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по ISBN")
    public void getBookByIsbnTest() {
        String isbn = "978";
        List<Book> books = bookDao.getByIsbn(isbn);

        List<Book> expected = AnyDaoTest.predefinedBooks.stream().filter(b -> b.getIsbn().contains(isbn)).collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по автору")
    public void getBookByAuthorTest() {
        String filter = "n";
        List<Book> books = bookDao.getByAuthor(filter);

        List<Book> expected = AnyDaoTest.predefinedBooks.stream().filter(
                b -> b.getAuthor().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

    @Test
    @DisplayName("Получение книг по жанру")
    public void getBookByGenreTest() {
        String filter = "e";
        List<Book> books = bookDao.getByGenre(filter);

        List<Book> expected = AnyDaoTest.predefinedBooks.stream().filter(
                b -> b.getGenre().getName().contains(filter))
                .collect(Collectors.toList());
        assertThat(books).containsAll(expected);
    }

}
