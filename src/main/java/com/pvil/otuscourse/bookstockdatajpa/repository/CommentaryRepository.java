package com.pvil.otuscourse.bookstockdatajpa.repository;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentaryRepository extends Repository<Commentary, Long> {
    List<Commentary> findByBook(Book book);

    Optional<Commentary> findById(Long id);

    void save(Commentary commentary);

    void deleteById(Long id);
}
