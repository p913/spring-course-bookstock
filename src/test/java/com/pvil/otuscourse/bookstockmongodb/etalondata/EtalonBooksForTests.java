package com.pvil.otuscourse.bookstockmongodb.etalondata;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EtalonBooksForTests {
    //Комментарии в БД и все последующие созданные д.б. с одинаковым временем с точностью до миллисекунды чтобы работал equals
    private static Date commentsDate = new Date();

    public static List<Book> getAll() {
        return Arrays.asList(
                new Book(1, "Harry Potter and the Sorcerer's Stone", "9780590353427", "Scholastic, Inc.", 1999,
                        new Author(1,"J. K. Rowling"), "Fantasy",
                                Arrays.asList(new Commentary(1, "Best book!", commentsDate, "Germiona"),
                                              new Commentary(2, "Bad!Bad!Bad!", commentsDate, "Lord Voldemort"))),
                new Book(2, "Harry Potter and the Goblet of Fire", "9780545582957", "Scholastic, Inc.", 2013,
                        new Author(1, "J. K. Rowling"), "Fantasy"),
                new Book(3, "Solaris", "9780156027601", "Mariner", 2002,
                        new Author(3, "Stanislav Lem"), "Science fiction"),
                new Book(4, "The Invincible", "9788363471545", "Pro Auctore Wojciech Zemek", 2017,
                        new Author(3, "Stanislav Lem"), "Science fiction"),
                new Book(5, "Anna Karenina", "9780679783305", "Modern Library", 2000,
                        new Author(2, "Lev Tolstoy"), "Drama"),
                new Book(6, "War and Peace", "9781400079988", "Vintage", 2008,
                        new Author(2, "Lev Tolstoy"), "Drama"),
                new Book(7, "Eugene Onegin: A Novel in Verse", "9780199538645", "Oxford University Press", 2009,
                        new Author(4, "A.S. Pushkin"), "Poetry")
        );

    }

    public static Book getNonExistent() {
        return new Book(8, "Non existing", "noIsbn", "noPublisher", 0,
                new Author(4, "A.S. Pushkin"), "Poetry");
    }

    public static Book getExistent() {
        return getAll().get(0);
    }

    public static Book getCanBeAdded() {
        return new Book(101, "Added", "noIsbn", "noPublisher", 0,
                new Author(4, "A.S. Pushkin"), "Poetry");
    }

    public static Book getCanBeDeleted() {
        return getAll().get(0);
    }

    public static Book getWithComments() {
        return getAll().get(0);
    }

    public static Commentary getCommentCanBeAdded() {
        return new Commentary(101, "Added", commentsDate, "Reader");
    }

    public static Commentary getCommentCanBeDeleted() {
        return getWithComments().getComments().get(0);
    }

    public static Commentary getNonExistentComment() {
        return new Commentary(3, "NonExistent", commentsDate, "Reader");
    }

}
