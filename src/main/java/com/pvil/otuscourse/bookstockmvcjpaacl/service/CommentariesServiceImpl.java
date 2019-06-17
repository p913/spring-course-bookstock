package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.CommentaryRepository;
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
