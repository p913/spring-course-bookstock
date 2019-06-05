package com.pvil.otuscourse.bookstockmvcformlogin.service;

import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcformlogin.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentariesServiceImpl implements CommentariesService {
    private final BookRepository bookRepository;

    public CommentariesServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Commentary> getAllForBook(Book book) {
        return bookRepository.findById(book.getId()).orElse(new Book()).getComments();
    }

    @Override
    public void add(Book book, Commentary commentary) {
        bookRepository.addComment(book, commentary);
    }

    @Override
    public boolean delete(Book book, Commentary commentary) {
        return bookRepository.deleteComment(book, commentary);
    }
}
