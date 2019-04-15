package com.pvil.otuscourse.bookstockdatajpa.etalondata;

import com.pvil.otuscourse.bookstockdatajpa.domain.Author;

import java.util.Arrays;
import java.util.List;

/**
 * Предосталяет данные, идентичные добавляемым в тестовую БД после ее создания
 */
public class EtalonAuthorsForTests {

    public static List<Author> getAll() {
        return Arrays.asList(
                new Author(1, "J. K. Rowling"),
                new Author(2, "Lev Tolstoy"),
                new Author(3, "Stanislav Lem"),
                new Author(4, "A.S. Pushkin"),
                new Author(5, "S. Esenin")
        );

    }

    public static Author getNonExistent() {
        return new Author(100, "V. Sorokin");
    }

    public static Author getExistent() {
        return new Author(1, "J. K. Rowling");
    }

    public static Author getWithoutReferences() {
        return new Author(5, "S. Esenin");
    }

}
