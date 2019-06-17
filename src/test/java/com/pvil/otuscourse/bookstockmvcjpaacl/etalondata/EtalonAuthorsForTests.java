package com.pvil.otuscourse.bookstockmvcjpaacl.etalondata;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Предосталяет данные, идентичные добавляемым в тестовую БД после ее создания
 */
public class EtalonAuthorsForTests {
    static List<Author> authors = Arrays.asList(
            new Author(1, "J. K. Rowling"),
                new Author(2, "Lev Tolstoy"),
                new Author(3, "Stanislav Lem"),
                new Author(4, "A.S. Pushkin"),
                new Author(5, "S. Esenin")
        );

    static Author getReferenceByName(String name) {
        Optional<Author> author = authors.stream().filter(a -> a.getName().equals(name)).findFirst();
        if (author.isEmpty())
            throw new EntityNotFoundException("No author " + name);
        return author.get();
    }

    public static List<Author> getAll() {
        List<Author> res = new ArrayList<>();
        authors.forEach(a -> res.add(new Author(a)));
        return res;
    }

    public static Author getExistent() {
        return new Author(authors.get(0));
    }

    public static Author getNonExistent() {
        return new Author(6, "Non existent");
    }

    public static Author getCanBeAdded() {
        return new Author("M.U. Lermontov");
    }

    public static Author getCanBeDeleted() {
        return new Author(authors.get(authors.size() - 1));
    }

    public static Author getCanNotBeDeletedDueToReferences() {
        return new Author(authors.get(0));
    }
}
