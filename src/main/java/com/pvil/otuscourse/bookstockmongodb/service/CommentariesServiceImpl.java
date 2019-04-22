package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import com.pvil.otuscourse.bookstockmongodb.repository.BookRepository;
import com.pvil.otuscourse.bookstockmongodb.repository.SequenceGeneratorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentariesServiceImpl implements CommentariesService {
    private final BookRepository bookRepository;

    private final SequenceGeneratorRepository sequenceGeneratorRepository;

    public CommentariesServiceImpl(BookRepository bookRepository, SequenceGeneratorRepository sequenceGeneratorRepository) {
        this.bookRepository = bookRepository;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
    }

    @Override
    public List<Commentary> getAllForBook(Book book) {
        return bookRepository.findById(book.getId()).orElse(new Book()).getComments();
    }

    @Override
    public void add(Book book, Commentary commentary) {
        commentary.setId(sequenceGeneratorRepository.generateSequence(Commentary.SEQUENCE_NAME));
        bookRepository.addComment(book, commentary);
    }

    @Override
    public boolean delete(Book book, Commentary commentary) {
        return bookRepository.deleteComment(book, commentary);
    }
}
