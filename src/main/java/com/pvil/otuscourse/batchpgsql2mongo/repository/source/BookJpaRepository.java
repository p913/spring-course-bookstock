package com.pvil.otuscourse.batchpgsql2mongo.repository.source;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookJpaRepository extends PagingAndSortingRepository<Book, Long> {
    @Override
    @Query("select b from Book b join fetch b.author join fetch b.genre")
    List<Book> findAll();

}
