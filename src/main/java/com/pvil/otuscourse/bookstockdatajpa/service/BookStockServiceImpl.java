package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Book;
import com.pvil.otuscourse.bookstockdatajpa.repository.BookRepository;
import com.pvil.otuscourse.bookstockdatajpa.utils.EntityResolver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookStockServiceImpl implements BookStockService {
    private final BookRepository bookRepository;

    private final AuthorsService authorsService;
    private final GenresService genresService;

    public BookStockServiceImpl(BookRepository bookRepository, AuthorsService authorsService, GenresService genresService) {
        this.bookRepository = bookRepository;
        this.authorsService = authorsService;
        this.genresService = genresService;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBookByCriterion(BookSearchCriterion criterion, String filter) {
        switch (criterion) {
            case NAME:
                return bookRepository.findByName(filter);

            case ISBN:
                return bookRepository.findByIsbn(filter);
            case YEAR:
                try {
                    return bookRepository.findByYear(Integer.parseInt(filter));
                } catch (NumberFormatException e) {
                }
                break;
            case AUTHOR:
                return bookRepository.findByAuthor(filter);
            case GENRE:
                return bookRepository.findByGenre(filter);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void addBook(Book book) {
        book.setAuthor(authorsService.resolveAuthor(book.getAuthor()));
        book.setGenre(genresService.resolveGenre(book.getGenre()));
        bookRepository.save(book);
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
    public boolean updateBook(Book book) {
        if (!bookRepository.existsById(book.getId()))
            return false;

        book.setAuthor(authorsService.resolveAuthor(book.getAuthor()));
        book.setGenre(genresService.resolveGenre(book.getGenre()));
        bookRepository.save(book);
        return true;
    }

    @Override
    public Book resolveBook(Book book) {
        return (Book)EntityResolver.resolve(
                new EntityResolver.ResolvingEntityWrapper() {
                    @Override
                    public long getId() {
                        return book.getId();
                    }

                    @Override
                    public boolean hasIdOnly() {
                        return book.getName() == null || book.getName().isEmpty();
                    }

                    @Override
                    public boolean isSame(Object to) {
                        if (to instanceof Book ) {
                            Book thiss = (Book) getEntity();
                            Book that = (Book) to;
                            return thiss.getId() == that.getId() &&
                                    thiss.getName().equals(that.getName());
                        } else
                            return false;
                    }

                    @Override
                    public Object getEntity() {
                        return book;
                    }
                },
                new EntityResolver.ResolvingEntityDao() {
                    @Override
                    public Optional getById(long id) {
                        return bookRepository.findById(id);
                    }

                    @Override
                    public List getBySample(EntityResolver.ResolvingEntityWrapper entity) {
                        return bookRepository.findByName(((Book)entity.getEntity()).getName());
                    }
                }

        );
    }
}
