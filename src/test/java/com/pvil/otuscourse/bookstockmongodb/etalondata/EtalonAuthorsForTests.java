package com.pvil.otuscourse.bookstockmongodb.etalondata;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;

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

    public static Author getExistent() {
        return new Author(1, "J. K. Rowling");
    }

    public static Author getNonExistent() {
        return new Author(6, "V. Sorokin");
    }

    public static Author getCanBeAdded() {
        return new Author(101, "M.U. Lermontov");
    }

    public static Author getCanBeDeleted() {
        return new Author(5, "S. Esenin");
    }

}
