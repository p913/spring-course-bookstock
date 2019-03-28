package com.pvil.otuscourse.bookstockjdbc.service;

import com.pvil.otuscourse.bookstockjdbc.dao.AuthorDao;
import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.utils.EntityResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorDao authorDao;

    public AuthorsServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    @Override
    public Optional<Author> getAuthorById(int id) {
        return authorDao.getById(id);
    }

    @Override
    public List<Author> getAuthorByName(String filter) {
        return authorDao.getByName(filter);
    }

    @Override
    public int addAuthor(Author author) {
        return authorDao.add(author);
    }

    @Override
    public boolean deleteAuthor(Author author) {
        return authorDao.delete(author);
    }

    @Override
    public boolean updateAuthor(Author author) {
        return authorDao.update(author);
    }

    @Override
    public Author resolveAuthor(Author author) {
        return EntityResolver.<Author, AuthorDao>resolve(author, authorDao);
    }

}
