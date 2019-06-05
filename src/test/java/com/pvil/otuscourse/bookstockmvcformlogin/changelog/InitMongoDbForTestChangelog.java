package com.pvil.otuscourse.bookstockmvcformlogin.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "001")
public class InitMongoDbForTestChangelog {

    @ChangeSet(order = "000", id = "dropDb", runAlways = true, author = "peter")
    public void initDb(MongoTemplate template) {
        template.getDb().drop();
    }

    @ChangeSet(order = "001", id = "addUsers", runAlways = true, author = "peter")
    public void initUsers(MongoTemplate template) {
        template.save(EtalonUsersForTests.getExistentReader());
    }

    @ChangeSet(order = "002", id = "addAuthors", runAlways = true, author = "peter")
    public void initAuthors(MongoTemplate template) {
        EtalonAuthorsForTests.authors.forEach(a -> template.save(a));
    }

    @ChangeSet(order = "003", id = "addBooks", runAlways = true, author = "peter")
    public void initBooks(MongoTemplate template) {
        EtalonBooksForTests.books.forEach(b -> template.save(b));
    }

}
