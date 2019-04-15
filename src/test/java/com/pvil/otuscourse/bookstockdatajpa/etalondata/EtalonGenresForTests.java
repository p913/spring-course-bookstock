package com.pvil.otuscourse.bookstockdatajpa.etalondata;

import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;

import java.util.Arrays;
import java.util.List;

public class EtalonGenresForTests {

    public static List<Genre> getAll() {
        return Arrays.asList(
                new Genre(1, "Fantasy"),
                new Genre(2, "Detective"),
                new Genre(3, "Drama"),
                new Genre(4, "Science fiction"),
                new Genre(5, "Poetry"),
                new Genre(6, "Horror")
        );
    }

    public static Genre getNonExistent() {
        return new Genre(100, "Life of great people");
    }

    public static Genre getExistent() {
        return new Genre(1, "Fantasy");
    }

    public static Genre getWithoutReferences() {
        return new Genre(6, "Horror");
    }
}
