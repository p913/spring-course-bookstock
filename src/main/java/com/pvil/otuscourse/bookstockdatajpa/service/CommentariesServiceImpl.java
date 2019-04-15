package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.domain.Commentary;
import com.pvil.otuscourse.bookstockdatajpa.repository.CommentaryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentariesServiceImpl implements CommentariesService {
    private final CommentaryRepository commentaryRepository;

    private final BookStockService bookStockService;

    public CommentariesServiceImpl(CommentaryRepository commentaryRepository, BookStockService bookStockService) {
        this.commentaryRepository = commentaryRepository;
        this.bookStockService = bookStockService;
    }

    @Override
    public List<Commentary> getAllForBook(Book book) {
        return commentaryRepository.findByBook(book);
    }

    @Override
    public void addCommentary(Commentary commentary) {
        commentary.setBook(bookStockService.resolveBook(commentary.getBook()));
        commentaryRepository.save(commentary);
    }

    @Override
    public boolean deleteCommentary(Commentary commentary) {
        try {
            commentaryRepository.deleteById(commentary.getId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }
}
