package com.pvil.otuscourse.bookstockmvcclassic.changelog;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Author;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EtalonBooksForTests {
    //Комментарии в БД и все последующие созданные д.б. с одинаковым временем с точностью до миллисекунды чтобы работал equals
    private static Date commentsDate = new Date();

    static List<Book> books = Arrays.asList(
            new Book(new ObjectId().toHexString(), "Harry Potter and the Sorcerer's Stone", "590353427", "Scholastic, Inc.", 1999,
                    EtalonAuthorsForTests.getReferenceByName("J. K. Rowling"), "Fantasy",
                    Arrays.asList(new Commentary(new ObjectId().toHexString(), "Best book!", commentsDate, "Germiona"),
                            new Commentary(new ObjectId().toHexString(), "Bad!Bad!Bad!", commentsDate, "Lord Voldemort"))),
            new Book(new ObjectId().toHexString(), "Harry Potter and the Goblet of Fire", "545582957", "Scholastic, Inc.", 2013,
                    EtalonAuthorsForTests.getReferenceByName("J. K. Rowling"), "Fantasy"),
            new Book(new ObjectId().toHexString(), "Solaris", "156027601", "Mariner", 2002,
                    EtalonAuthorsForTests.getReferenceByName("Stanislav Lem"), "Science fiction"),
            new Book(new ObjectId().toHexString(), "The Invincible", "836347154", "Pro Auctore Wojciech Zemek", 2017,
                    EtalonAuthorsForTests.getReferenceByName("Stanislav Lem"), "Science fiction"),
            new Book(new ObjectId().toHexString(), "Anna Karenina", "679783305", "Modern Library", 2000,
                    EtalonAuthorsForTests.getReferenceByName("Lev Tolstoy"), "Drama"),
            new Book(new ObjectId().toHexString(), "War and Peace", "400079988", "Vintage", 2008,
                    EtalonAuthorsForTests.getReferenceByName("Lev Tolstoy"), "Drama"),
            new Book(new ObjectId().toHexString(), "Eugene Onegin: A Novel in Verse", "199538645", "Oxford University Press", 2009,
                    EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"), "Poetry")
    );

    public static List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        books.forEach(b -> books.add(new Book(b)));
        return books;
    }

    public static Book getNonExistent() {
        return new Book("Non existing", "000000000", "noPublisher", 2000,
                EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"), "Poetry");
    }

    public static Book getExistent() {
        return new Book(books.get(1));
    }

    public static Book getCanBeAdded() {
        return new Book("Added", "000000000", "noPublisher", 2000,
                EtalonAuthorsForTests.getReferenceByName("A.S. Pushkin"), "Poetry");
    }

    public static Book getCanBeDeleted() {
        return new Book(books.get(0));
    }

    public static Book getWithComments() {
        return new Book(books.get(0));
    }

    public static Commentary getCommentCanBeAdded() {
        return new Commentary("Added", commentsDate, "Reader");
    }

    public static Commentary getCommentCanBeDeleted() {
        return getWithComments().getComments().get(0);
    }

}
