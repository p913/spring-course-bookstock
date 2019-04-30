package com.pvil.otuscourse.bookstockmvcclassic.changelog;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Author;
import com.pvil.otuscourse.bookstockmvcclassic.service.EntityNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Предосталяет данные, идентичные добавляемым в тестовую БД после ее создания
 */
public class EtalonAuthorsForTests {
    static List<Author> authors = Arrays.asList(
            new Author(new ObjectId().toHexString(), "J. K. Rowling"),
                new Author(new ObjectId().toHexString(), "Lev Tolstoy"),
                new Author(new ObjectId().toHexString(), "Stanislav Lem"),
                new Author(new ObjectId().toHexString(), "A.S. Pushkin"),
                new Author(new ObjectId().toHexString(), "S. Esenin")
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
