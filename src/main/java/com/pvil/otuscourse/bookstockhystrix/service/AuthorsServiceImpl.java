package com.pvil.otuscourse.bookstockhystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.pvil.otuscourse.bookstockhystrix.domain.Author;
import com.pvil.otuscourse.bookstockhystrix.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockhystrix.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public AuthorsServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-get-all",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-get",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public Optional<Author> getById(String id) {
        return authorRepository.findById(id);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-get",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Author> getByPartName(String filter) {
        return authorRepository.findByNameContaining(filter);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-get",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public List<Author> getByName(String name) {
        return authorRepository.findByName(name);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-modify",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-modify",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public void delete(Author author) {
        if (bookRepository.existsByAuthor(author))
            throw new EntityHasReferences(String.format("There are book that write author with Id = %d", author.getId()));

        authorRepository.deleteById(author.getId());
    }

    @HystrixCommand(groupKey = "db-repo",
            commandKey = "author-provide",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    @Override
    public Author getExistingElseCreate(Author author) {
        if (author.getId() != null ) {
            Optional<Author> founded = authorRepository.findById(author.getId());
            if (founded.isEmpty())
                throw new EntityNotFoundException(String.format("Author with Id = %s not found", author.getId()));
            else if (author.getName() == null || author.getName().isEmpty())
                return founded.get();
            else if (!author.equals(founded.get()))
                throw new EntityNotFoundException(String.format("Author with Id = %s is different author than %s (%s)",
                        author.getId(), author.toString(), founded.get().toString()));
            else
                return founded.get();
        } else {
            List<Author> entities = authorRepository.findByName(author.getName());
            if (entities.isEmpty())
                return authorRepository.save(author);
            else
                return entities.get(0);
        }
    }
}
