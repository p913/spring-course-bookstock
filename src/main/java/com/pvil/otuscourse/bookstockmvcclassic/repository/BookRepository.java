package com.pvil.otuscourse.bookstockmvcclassic.repository;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
    @Query(value="{}", fields="{comments: 0}")
    List<Book> findAllExcludeComments();

    @Query(value="{title: ?0}", fields="{comments: 0}")
    List<Book> findByTitleExcludeComments(String title);
}
