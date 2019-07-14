package com.pvil.otuscourse.bookstockhystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.pvil.otuscourse.bookstockhystrix.domain.Book;
import com.pvil.otuscourse.bookstockhystrix.repository.BookRepository;
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

    @HystrixCommand(groupKey = "db-repo",
            fallbackMethod = "getAllFallback",
            commandKey = "book-get-all",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"), //Максимальное время выполнения
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"), //Минимальное количество проваленных запросов до "перегорания предохранителя"
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") //Время "восстановления предохранителя"
            })
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    private List<Book> getAllFallback() {
        return Collections.EMPTY_LIST;
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "book-get",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Book> getByTitle(String title) {
        return bookRepository.findByTitleExcludeComments(title);
    }

    @HystrixCommand(groupKey = "db-repo",
            fallbackMethod = "getContainsAnyOfFallback",
            commandKey = "book-search",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Book> getContainsAnyOf(String searchText) {
        return bookRepository.findByAnythingContainsExcludeComments(searchText);
    }

    private List<Book> getContainsAnyOfFallback(String searchText) {
        return Collections.EMPTY_LIST;
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "book-get",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public Optional<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "book-modify",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public void save(Book book) {
        book.setAuthor(authorsService.getExistingElseCreate(book.getAuthor()));
        bookRepository.save(book);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "book-modify",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public void delete(Book book) {
        bookRepository.deleteById(book.getId());
    }


}
