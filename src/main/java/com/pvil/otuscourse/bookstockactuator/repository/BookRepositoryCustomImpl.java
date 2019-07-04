package com.pvil.otuscourse.bookstockactuator.repository;

import com.pvil.otuscourse.bookstockactuator.domain.Author;
import com.pvil.otuscourse.bookstockactuator.domain.Book;
import com.pvil.otuscourse.bookstockactuator.domain.Commentary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
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
        commentary.setId(new ObjectId().toHexString());
        mongoOperations.updateFirst(query(where("_id").is(book.getId())),
                new Update().addToSet("comments", commentary),
                Book.class);
    }

    @Override
    public boolean deleteComment(Book book, Commentary commentary) {
        return 0 != mongoOperations.updateFirst(query(where("_id").is(book.getId())),
                new Update().pull("comments", commentary),
                Book.class)
                .getModifiedCount();
    }

    @Override
    public boolean existsByAuthor(Author author) {
        return mongoOperations.exists(query(where("author.$id").is(new ObjectId(author.getId()))), Book.class);
    }

    @Override
    @Query(fields = "{comments: 0")
    public List<Book> findByAnythingContainsExcludeComments(String filter) {
        String regex = ".*" + filter + ".*";
        List<ObjectId> matchedAuthorsId = authorRepository.findByNameContaining(filter)
                                            .stream().map(a -> new ObjectId(a.getId())).collect(Collectors.toList());

        List<Criteria> orConditions = new ArrayList<>();
        if (!matchedAuthorsId.isEmpty())
            orConditions.add(where("author.$id").in(matchedAuthorsId));
        orConditions.add(where("title").regex(regex));
        orConditions.add(where("genre").regex(regex));
        orConditions.add(where("publisher").regex(regex));
        orConditions.add(where("isbn").regex(regex));
        try {
            orConditions.add(where("year").is(Integer.parseInt(filter)));
        } catch (NumberFormatException e) {
        }

        return mongoOperations.find(
                query(new Criteria().orOperator(orConditions.toArray(new Criteria[] {}))), Book.class);
    }

}
