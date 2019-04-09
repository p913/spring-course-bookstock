package com.pvil.otuscourse.bookstockjpajpql.service;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;
import com.pvil.otuscourse.bookstockjpajpql.repository.CommentaryRepository;
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
        return commentaryRepository.getAllForBook(book);
    }

    @Override
    public void addCommentary(Commentary commentary) {
        commentary.setBook(bookStockService.resolveBook(commentary.getBook()));
        commentaryRepository.add(commentary);
    }

    @Override
    public boolean deleteCommentary(Commentary commentary) {
        return commentaryRepository.delete(commentary);
    }
}
