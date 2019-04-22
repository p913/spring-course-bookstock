package com.pvil.otuscourse.bookstockmongodb.service;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookRepository bookRepository;

    private final AuthorsService authorsService;

    public BookStockServiceImpl(BookRepository bookRepository, AuthorsService authorsService) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public List<Book> getByCriterion(BookSearchCriterion criterion, String filter) {
        switch (criterion) {
            case TITLE:
                return bookRepository.findByTitleContains(filter);
            case ISBN:
                return bookRepository.findByIsbnContains(filter);
            case YEAR:
                try {
                    return bookRepository.findByYear(Integer.parseInt(filter));
                } catch (NumberFormatException e) {
                }
                break;
            case AUTHOR:
                return bookRepository.findByAuthorContains(filter);
            case GENRE:
                return bookRepository.findByGenreContains(filter);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Optional<Book> getById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void add(Book book) {
        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        if (!bookRepository.existsById(book.getId()))
            return false;

        bookRepository.deleteById(book.getId());
        return true;
    }

    @Override
    public boolean update(Book book) {
        if (!bookRepository.existsById(book.getId()))
            return false;

        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        bookRepository.save(book);
        return true;
    }

}
