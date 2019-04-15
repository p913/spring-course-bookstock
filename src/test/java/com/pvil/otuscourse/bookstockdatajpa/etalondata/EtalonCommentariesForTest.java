package com.pvil.otuscourse.bookstockdatajpa.etalondata;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class EtalonCommentariesForTest {
    private static final ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();

    public static List<Commentary> getAll() {
        return Arrays.asList(
                new Commentary(1, "I kill him!",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T13:00:00"), zoneOffset),"Dantes", new Book(7)),
                new Commentary(2, "It's beautiful! Pushkin is my teacher!",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T14:00:00"), zoneOffset), "Lermontov", new Book(7)),
                new Commentary(3, "I sleep on second page...",
                        OffsetDateTime.of(LocalDateTime.parse("2019-04-07T15:00:00"), zoneOffset), "Vasia Pupkin", new Book(6))
                );
    }

    public static Commentary getNonExistent() {
        return new Commentary(4, "commentary",
                OffsetDateTime.of(LocalDateTime.parse("2019-04-08T23:59:59"), zoneOffset),"Commenter", new Book(7));
    }

    public static Commentary getExistent() {
        return getAll().get(0);
    }

}
