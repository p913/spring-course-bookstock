package com.pvil.otuscourse.bookstockwebflux.repository;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final ReactiveMongoOperations mongoOperations;

    private final AuthorRepository authorRepository;

    public BookRepositoryCustomImpl(ReactiveMongoOperations mongoOperations, AuthorRepository authorRepository) {
        this.mongoOperations = mongoOperations;
        this.authorRepository = authorRepository;
    }

    @Override
    public Mono<Commentary> addComment(Book book, Commentary commentary) {
        commentary.setId(new ObjectId().toHexString());
        return mongoOperations.updateFirst(query(where("_id").is(book.getId())),
                    new Update().addToSet("comments", commentary),
                    Book.class)
                .map(r -> commentary);
    }

    @Override
    public Mono<Boolean> deleteComment(Book book, Commentary commentary) {
        return mongoOperations.updateFirst(query(where("_id").is(book.getId())),
                    new Update().pull("comments", commentary),
                    Book.class)
                .map(r -> r.getModifiedCount() != 0);
    }

    @Override
    public Mono<Boolean> existsByAuthor(Author author) {
        return mongoOperations.exists(query(where("author.$id").is(new ObjectId(author.getId()))), Book.class);
    }

    @Override
    @Query(fields = "{comments: 0")
    public Flux<Book> findByAnythingContainsExcludeComments(String filter) {
        String regex = ".*" + filter + ".*";

        List<Criteria> orConditions = new ArrayList<>();
        orConditions.add(where("title").regex(regex));
        orConditions.add(where("genre").regex(regex));
        orConditions.add(where("publisher").regex(regex));
        orConditions.add(where("isbn").regex(regex));
        try {
            orConditions.add(where("year").is(Integer.parseInt(filter)));
        } catch (NumberFormatException e) {
        }

        return authorRepository
                .findByNameContaining(filter)
                .map(a -> new ObjectId(a.getId()))
                .collectList()
                .flatMapMany(la -> {
                    if (!la.isEmpty())
                        orConditions.add(where("author.$id").in(la));
                    return mongoOperations.find(
                            query(new Criteria().orOperator(orConditions.toArray(new Criteria[] {}))), Book.class);
                });
    }

}
