package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.BookRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookRepository bookRepository;

    private final AuthorsService authorsService;

    private final GenresService genresService;

    private final UserPermissionsForBookService userPermissionsForBookService;

    public BookStockServiceImpl(BookRepository bookRepository, AuthorsService authorsService,
                                GenresService genresService, UserPermissionsForBookService userPermissionsForBookService) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
        this.genresService = genresService;
        this.userPermissionsForBookService = userPermissionsForBookService;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String searchText) {
        return bookRepository.findByAnythingContains(searchText);
    }

    @Override
    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public boolean deleteBook(Book book) {
        try {
            bookRepository.deleteById(book.getId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public void addBook(Book book) {
        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        book.setGenre(genresService.getExistingElseCreate(book.getGenre()));
        bookRepository.save(book);

        //Создав книгу - добавляем права по умолчанию
        userPermissionsForBookService.grantDefaults(book.getId());
    }

    @Override
    public boolean updateBook(Book book) {
        if (!bookRepository.existsById(book.getId()))
            return false;

        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        book.setGenre(genresService.getExistingElseCreate(book.getGenre()));
        bookRepository.save(book);

        return true;
    }


}

