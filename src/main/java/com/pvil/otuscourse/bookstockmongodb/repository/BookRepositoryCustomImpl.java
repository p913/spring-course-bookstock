package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final MongoOperations mongoOperations;

    private final AuthorRepository authorRepository;

    public BookRepositoryCustomImpl(MongoOperations mongoOperations, AuthorRepository authorRepository) {
        this.mongoOperations = mongoOperations;
        this.authorRepository = authorRepository;
    }

    @Override
    public void addComment(Book book, Commentary commentary) {
        mongoOperations.updateFirst(query(where("id").is(book.getId())),
                new Update().addToSet("comments", commentary),
                Book.class);
    }

    @Override
    public boolean deleteComment(Book book, Commentary commentary) {
        return 0 != mongoOperations.updateFirst(query(where("id").is(book.getId())),
                new Update().pull("comments", commentary),
                Book.class)
                .getModifiedCount();
    }

    @Override
    public List<Book> findByAuthorContains(String filter) {
        return mongoOperations.find(query(where("author.$id")
                .in(authorRepository.findByNameContaining(filter)
                        .stream().map(a -> a.getId()).collect(Collectors.toList()))),
                Book.class);
    }

    @Override
    public boolean existsByAuthor(Author author) {
        return mongoOperations.exists(query(where("author.$id").is(author.getId())), Book.class);
    }
}
