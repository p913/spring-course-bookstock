package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.AuthorRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Author getExistingElseCreate(Author author) {
        if (author.getId() != 0 ) {
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
