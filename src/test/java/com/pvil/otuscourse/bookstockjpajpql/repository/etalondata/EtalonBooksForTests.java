package com.pvil.otuscourse.bookstockjpajpql.repository.etalondata;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;

import java.util.Arrays;
import java.util.List;

public class EtalonBooksForTests {
    public static List<Book> getAll() {
        return Arrays.asList(
                new Book(1, "Harry Potter and the Sorcerer's Stone", "9780590353427", "Scholastic, Inc.", 1999,
                        new Author(1,"J. K. Rowling"), new Genre(1, "Fantasy")),
                new Book(2, "Harry Potter and the Goblet of Fire", "9780545582957", "Scholastic, Inc.", 2013,
                        new Author(1, "J. K. Rowling"), new Genre(1, "Fantasy")),
                new Book(3, "Solaris", "9780156027601", "Mariner", 2002,
                        new Author(3, "Stanislav Lem"), new Genre(4, "Science fiction")),
                new Book(4, "The Invincible", "9788363471545", "Pro Auctore Wojciech Zemek", 2017,
                        new Author(3, "Stanislav Lem"), new Genre(4, "Science fiction")),
                new Book(5, "Anna Karenina", "9780679783305", "Modern Library", 2000,
                        new Author(2, "Lev Tolstoy"), new Genre(3, "Drama")),
                new Book(6, "War and Peace", "9781400079988", "Vintage", 2008,
                        new Author(2, "Lev Tolstoy"), new Genre(3, "Drama")),
                new Book(7, "Eugene Onegin: A Novel in Verse", "9780199538645", "Oxford University Press", 2009,
                        new Author(4, "A.S. Pushkin"), new Genre(5,"Poetry"))
        );

    }

    public static Book getNonExistent() {
        return new Book(100, "Non existing", "noIsbn", "noPublisher", 0,
                new Author(4, "A.S. Pushkin"), new Genre(5,"Poetry"));
    }

    public static Book getExistent() {
        return getAll().get(0);
    }

    public static Book getWithReferences() {
        return getAll().get(6);
    }

    public static Book getWithoutReferences() {
        return getAll().get(0);
    }
}
