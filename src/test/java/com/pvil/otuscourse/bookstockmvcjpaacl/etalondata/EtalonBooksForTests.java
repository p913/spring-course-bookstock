package com.pvil.otuscourse.bookstockmvcjpaacl.etalondata;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EtalonBooksForTests {
    static List<Book> books = Arrays.asList(
            new Book(1, "Harry Potter and the Sorcerer's Stone", "9780590353427", "Scholastic, Inc.", 1999,
                    EtalonAuthorsForTests.getReferenceByName("J. K. Rowling"),
                    EtalonGenresForTests.getReferenceByName("Fantasy")),
            new Book(2, "Harry Potter and the Goblet of Fire", "9780545582957", "Scholastic, Inc.", 2013,
                    EtalonAuthorsForTests.getReferenceByName("J. K. Rowling"),
                    EtalonGenresForTests.getReferenceByName("Fantasy")),
            new Book(3, "Solaris", "9780156027601", "Mariner", 2002,
                    EtalonAuthorsForTests.getReferenceByName("Stanislav Lem"),
                    EtalonGenresForTests.getReferenceByName("Science fiction")),
            new Book(4, "The Invincible", "9788363471545", "Pro Auctore Wojciech Zemek", 2017,
                    EtalonAuthorsForTests.getReferenceByName("Stanislav Lem"),
                    EtalonGenresForTests.getReferenceByName("Science fiction")),
            new Book(5, "Anna Karenina", "9780679783305", "Modern Library", 2000,
                    EtalonAuthorsForTests.getReferenceByName("Lev Tolstoy"),
                    EtalonGenresForTests.getReferenceByName("Drama")),
            new Book(6, "War and Peace", "9781400079988", "Vintage", 2008,
                    EtalonAuthorsForTests.getReferenceByName("Lev Tolstoy"),
                    EtalonGenresForTests.getReferenceByName("Drama")),
            new Book(7, "Eugene Onegin: A Novel in Verse", "9780199538645", "Oxford University Press", 2009,
                    EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"),
                    EtalonGenresForTests.getReferenceByName("Poetry"))
    );

    public static List<Book> getAll() {
        List<Book> res = new ArrayList<>();
        books.forEach(b -> res.add(new Book(b)));
        return res;
    }

    public static Book getNonExistent() {
        return new Book("Non existing", "0000000000", "noPublisher", 2000,
                EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"),
                EtalonGenresForTests.getReferenceByName("Poetry"));
    }

    public static Book getExistent() {
        return new Book(books.get(1));
    }

    public static Book getCanBeAdded() {
        return new Book("Added", "0000000000", "noPublisher", 2000,
                EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"),
                EtalonGenresForTests.getReferenceByName("Poetry"));
    }

    public static Book getCanBeDeleted() {
        return new Book(books.get(0));
    }

    public static Book getWithComments() {
        return new Book(EtalonCommentariesForTest.getExistent().getBook().getId());
    }

}
