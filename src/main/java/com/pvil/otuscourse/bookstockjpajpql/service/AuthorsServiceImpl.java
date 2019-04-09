package com.pvil.otuscourse.bookstockjpajpql.service;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import com.pvil.otuscourse.bookstockjpajpql.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockjpajpql.utils.EntityResolver;
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
        return authorRepository.getAll();
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return authorRepository.getById(id);
    }

    @Override
    public List<Author> getAuthorByName(String filter) {
        return authorRepository.getByName(filter);
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.add(author);
    }

    @Override
    public boolean deleteAuthor(Author author) {
        return authorRepository.delete(author);
    }

    @Override
    public boolean updateAuthor(Author author) {
        return authorRepository.update(author);
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
                        return authorRepository.getById(id);
                    }

                    @Override
                    public List getBySample(EntityResolver.ResolvingEntityWrapper entity) {
                        return authorRepository.getByName(((Author)entity.getEntity()).getName());
                    }
                }

        );
    }

}
