package com.pvil.otuscourse.bookstockactuator.service;

import com.pvil.otuscourse.bookstockactuator.domain.Book;
import com.pvil.otuscourse.bookstockactuator.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookRepository bookRepository;

    private final AuthorsService authorsService;

    private final BookSearchMetricsService bookSearchMetricsService;

    public BookStockServiceImpl(BookRepository bookRepository, AuthorsService authorsService,
                                BookSearchMetricsService bookSearchMetricsService) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
        this.bookSearchMetricsService = bookSearchMetricsService;
    }

    @Override
    public List<Book> getAll() {
        bookSearchMetricsService.alive();

        return bookRepository.findAll();
    }

    @Override
    public List<Book> getByTitle(String title) {
        return bookRepository.findByTitleExcludeComments(title);
    }

    @Override
    public List<Book> getContainsAnyOf(String searchText) {
        List<Book> res =  bookRepository.findByAnythingContainsExcludeComments(searchText);

        bookSearchMetricsService.alive();
        bookSearchMetricsService.countSearchCase(res.size());

        return res;
    }

    @Override
    public Optional<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public void save(Book book) {
        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.deleteById(book.getId());
    }


}
