package com.pvil.otuscourse.bookstockmvcformlogin.repository;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
    @Query(value="{}", fields="{comments: 0}")
    List<Book> findAllExcludeComments();

    @Query(value="{title: ?0}", fields="{comments: 0}")
    List<Book> findByTitleExcludeComments(String title);
}
