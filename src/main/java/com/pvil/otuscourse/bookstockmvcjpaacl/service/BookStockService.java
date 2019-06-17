package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> getAllBooks();

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> searchBooks(String searchText);

    @PreAuthorize("hasPermission(#id, 'com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book', 'READ')")
    Optional<Book> getBookById(long id);

    //У новой книги id=0, проверяем права на CREATE у бизнес-сущности с кодом 0, а такая должна была быть
    //создана при старте приложения
    @PreAuthorize("hasPermission(#book, 'CREATE')")
    void addBook(Book book);

    @PreAuthorize("hasPermission(#book, 'DELETE')")
    boolean deleteBook(Book book);

    @PreAuthorize("hasPermission(#book, 'WRITE')")
    boolean updateBook(Book book);

}
