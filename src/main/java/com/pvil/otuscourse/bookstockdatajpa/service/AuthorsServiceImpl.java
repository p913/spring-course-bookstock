package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Author;
import com.pvil.otuscourse.bookstockdatajpa.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockdatajpa.utils.EntityResolver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorRepository authorRepository;

    public AuthorsServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> getAuthorByName(String filter) {
        return authorRepository.findByNameContaining(filter);
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public boolean deleteAuthor(Author author) {
        try {
            authorRepository.deleteById(author.getId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateAuthor(Author author) {
        if (!authorRepository.existsById(author.getId()))
            return false;

        authorRepository.save(author);
        return true;
    }

    @Override
    public Author resolveAuthor(Author author) {
        return (Author) EntityResolver.resolve(
                new EntityResolver.ResolvingEntityWrapper() {
                   @Override
                   public long getId() {
                       return author.getId();
                   }

                   @Override
                   public boolean hasIdOnly() {
                       return author.getName() == null || author.getName().isEmpty();
                   }

                    @Override
                    public boolean isSame(Object to) {
                        return getEntity().equals(to);
                    }

                    @Override
                    public Object getEntity() {
                           return author;
                       }
                   },
                new EntityResolver.ResolvingEntityDao() {
                    @Override
                    public Optional getById(long id) {
                        return authorRepository.findById(id);
                    }

                    @Override
                    public List getBySample(EntityResolver.ResolvingEntityWrapper entity) {
                        return authorRepository.findByNameContaining(((Author)entity.getEntity()).getName());
                    }
                }

        );
    }

}
