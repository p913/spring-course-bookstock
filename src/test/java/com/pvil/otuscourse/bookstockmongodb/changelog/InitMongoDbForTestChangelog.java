package com.pvil.otuscourse.bookstockmongodb.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import com.pvil.otuscourse.bookstockmongodb.domain.DatabaseSequence;
import com.pvil.otuscourse.bookstockmongodb.etalondata.EtalonAuthorsForTests;
import com.pvil.otuscourse.bookstockmongodb.etalondata.EtalonBooksForTests;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "001")
public class InitMongoDbForTestChangelog {

    @ChangeSet(order = "001", id = "addSeq", runAlways = true, author = "peter")
    public void initSequenceGenerators(MongoTemplate template) {
        template.save(new DatabaseSequence(Author.SEQUENCE_NAME, 100));
        template.save(new DatabaseSequence(Book.SEQUENCE_NAME, 100));
        template.save(new DatabaseSequence(Commentary.SEQUENCE_NAME, 100));
    }

    @ChangeSet(order = "002", id = "addAuthors", runAlways = true, author = "peter")
    public void initAuthors(MongoTemplate template) {
        EtalonAuthorsForTests.getAll().forEach(a -> template.save(a));
    }

    @ChangeSet(order = "003", id = "addBooks", runAlways = true, author = "peter")
    public void initBooks(MongoTemplate template) {
        EtalonBooksForTests.getAll().forEach(b -> template.save(b));
    }

}
