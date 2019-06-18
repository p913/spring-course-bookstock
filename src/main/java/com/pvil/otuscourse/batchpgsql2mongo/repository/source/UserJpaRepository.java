package com.pvil.otuscourse.batchpgsql2mongo.repository.source;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends PagingAndSortingRepository<User, Long> {
}
