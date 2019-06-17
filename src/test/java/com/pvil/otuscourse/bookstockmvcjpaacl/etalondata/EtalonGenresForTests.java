package com.pvil.otuscourse.bookstockmvcjpaacl.etalondata;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Genre;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EtalonGenresForTests {

    private static List<Genre> genres = Arrays.asList(
                new Genre(1, "Fantasy"),
                new Genre(2, "Detective"),
                new Genre(3, "Drama"),
                new Genre(4, "Science fiction"),
                new Genre(5, "Poetry"),
                new Genre(6, "Horror")
        );

    static Genre getReferenceByName(String name) {
        Optional<Genre> genre = genres.stream().filter(a -> a.getName().equals(name)).findFirst();
        if (genre.isEmpty())
            throw new EntityNotFoundException("No genre " + name);
        return genre.get();
    }

    public static List<Genre> getAll() {
        List<Genre> res = new ArrayList<>();
        genres.forEach(a -> res.add(new Genre(a)));
        return res;
    }

    public static Genre getExistent() {
        return new Genre(genres.get(0));
    }

    public static Genre getNonExistent() {
        return new Genre(7, "Non existent");
    }

    public static Genre getCanBeAdded() {
        return new Genre("M.U. Lermontov");
    }

    public static Genre getCanBeDeleted() {
        return new Genre(genres.get(genres.size() - 1));
    }

    public static Genre getCanNotBeDeletedDueToReferences() {
        return new Genre(genres.get(0));
    }
}
