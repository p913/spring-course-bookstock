package com.pvil.otuscourse.bookstockhystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.pvil.otuscourse.bookstockhystrix.domain.Book;
import com.pvil.otuscourse.bookstockhystrix.domain.Commentary;
import com.pvil.otuscourse.bookstockhystrix.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentariesServiceImpl implements CommentariesService {
    private final BookRepository bookRepository;

    public CommentariesServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @HystrixCommand(groupKey = "db-repo",
            fallbackMethod = "getAllForBookFallback",
            commandKey = "comment-get-all",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Commentary> getAllForBook(Book book) {
        return bookRepository.findById(book.getId()).orElse(new Book()).getComments();
    }

    private List<Commentary> getAllForBookFallback() {
        return Collections.EMPTY_LIST;
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "comment-add",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public void add(Book book, Commentary commentary) {
        bookRepository.addComment(book, commentary);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "comment-delete",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public boolean delete(Book book, Commentary commentary) {
        return bookRepository.deleteComment(book, commentary);
    }
}
