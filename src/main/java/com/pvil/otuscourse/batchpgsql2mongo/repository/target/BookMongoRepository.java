package com.pvil.otuscourse.batchpgsql2mongo.repository.target;

import com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMongoRepository extends MongoRepository<Book, String> {
}
