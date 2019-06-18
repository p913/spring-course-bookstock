package com.pvil.otuscourse.batchpgsql2mongo.repository.target;

import com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorMongoRepository  extends MongoRepository<Author, String> {
    Optional<Author> findByName(String name);
}
