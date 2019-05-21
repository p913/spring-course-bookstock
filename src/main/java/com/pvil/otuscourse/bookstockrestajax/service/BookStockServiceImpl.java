package com.pvil.otuscourse.bookstockrestajax.service;

import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.repository.BookRepository;
import org.springframework.stereotype.Service;

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
    public List<Book> getByTitle(String title) {
        return bookRepository.findByTitleExcludeComments(title);
    }

    @Override
    public List<Book> getContainsAnyOf(String searchText) {
        return bookRepository.findByAnythingContainsExcludeComments(searchText);
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
