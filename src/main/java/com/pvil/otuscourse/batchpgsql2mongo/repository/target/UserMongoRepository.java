package com.pvil.otuscourse.batchpgsql2mongo.repository.target;

import com.pvil.otuscourse.batchpgsql2mongo.domain.target.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
}
