package com.pvil.otuscourse.batchpgsql2mongo.repository.source;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorJpaRepository extends PagingAndSortingRepository<Author, Long> {
}
