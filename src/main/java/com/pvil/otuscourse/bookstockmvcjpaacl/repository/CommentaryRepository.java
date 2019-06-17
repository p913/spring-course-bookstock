package com.pvil.otuscourse.bookstockmvcjpaacl.repository;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentaryRepository extends Repository<Commentary, Long> {
    @Query("select c from Commentary c join fetch c.user join c.book where c.book = :book")
    List<Commentary> findByBook(Book book);

    Optional<Commentary> findById(Long id);

    void save(Commentary commentary);

    void deleteById(Long id);
}
