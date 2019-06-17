package com.pvil.otuscourse.bookstockmvcjpaacl.etalondata;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EtalonCommentariesForTest {
    private static final ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();

    private static List<Commentary> comments = Arrays.asList(
                new Commentary(1, "Great!",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T13:00:00"), zoneOffset),
                        EtalonUsersForTests.getReferenceToReader(), new Book(7)),
                new Commentary(2, "It's beautiful!",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T14:00:00"), zoneOffset),
                        EtalonUsersForTests.getReferenceToReader(), new Book(7)),
                new Commentary(3, "I sleep on second page...",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T15:00:00"), zoneOffset),
                        EtalonUsersForTests.getReferenceToReader(), new Book(6))
                );

    public static List<Commentary> getAllForBook(Book book) {
        return comments
                .stream()
                .filter(a -> a.getBook().getId() == book.getId())
                .collect(Collectors.toList());
    }

    public static Commentary getNonExistent() {
        return new Commentary(4, "Non existent commentary",
                OffsetDateTime.of(LocalDateTime.parse("2019-04-08T23:59:59"), zoneOffset),
                EtalonUsersForTests.getReferenceToReader(), new Book(7));
    }

    public static Commentary getCanBeAdded() {
        return new Commentary("Added commentary",
                OffsetDateTime.of(LocalDateTime.parse("2019-04-08T23:59:59"), zoneOffset),
                EtalonUsersForTests.getReferenceToReader(), new Book(7));
    }

    public static Commentary getCanBeDeleted() {
        return comments.get(0);
    }

    public static Commentary getExistent() {
        return comments.get(0);
    }

}
