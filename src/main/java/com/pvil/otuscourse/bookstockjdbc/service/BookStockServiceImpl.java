package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.dao.BookDao;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookDao bookDao;

    private final AuthorsService authorsService;
    private final GenresService genresService;

    public BookStockServiceImpl(BookDao bookDao, AuthorsService authorsService, GenresService genresService) {
        this.bookDao = bookDao;
        this.authorsService = authorsService;
        this.genresService = genresService;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public List<Book> getBookByName(String nameFilter) {
        return bookDao.getByName(nameFilter);
    }

    @Override
    public List<Book> getBookByGenre(String genreFilter) {
        return bookDao.getByGenre(genreFilter);
    }

    @Override
    public List<Book> getBookByAuthor(String authorFilter) {
        return bookDao.getByAuthor(authorFilter);
    }

    @Override
    public List<Book> getBookByIsbn(String isbnFilter) {
        return bookDao.getByIsbn(isbnFilter);
    }

    @Override
    public List<Book> getBookByYear(int year) {
        return bookDao.getByYear(year);
    }

    @Override
    public Optional<Book> getBookById(int id) {
        return bookDao.getById(id);
    }

    @Override
    public int addBook(Book book) {
        book.setAuthor(authorsService.resolveAuthor(book.getAuthor()));
        book.setGenre(genresService.resolveGenre(book.getGenre()));
        return bookDao.add(book);
    }



    @Override
    public boolean deleteBook(Book book) {
        return bookDao.delete(book);
    }

    @Override
    public boolean updateBook(Book book) {
        book.setAuthor(authorsService.resolveAuthor(book.getAuthor()));
        book.setGenre(genresService.resolveGenre(book.getGenre()));
        return bookDao.update(book);
    }
}
