package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByName(String nameFilter) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre where b.name like :filter", Book.class);
        query.setParameter("filter", "%" + nameFilter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByGenre(String genreFilter) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre g where g.name like :filter", Book.class);
        query.setParameter("filter", "%" + genreFilter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByAuthor(String authorFilter) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author a join fetch b.genre where a.name like :filter", Book.class);
        query.setParameter("filter", "%" + authorFilter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByIsbn(String isbnFilter) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre g where b.isbn like :filter", Book.class);
        query.setParameter("filter", "%" + isbnFilter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByYear(int year) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre g where b.year = :year", Book.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(Book book) {
        em.persist(book);
    }

    @Override
    @Transactional
    public boolean delete(Book book) {
        Book deleting = em.find(Book.class, book.getId());
        if (deleting == null)
            return false;

        em.remove(deleting);
        em.flush();
        return true;
    }

    @Override
    @Transactional
    public boolean update(Book book) {
        if (em.find(Book.class, book.getId()) == null)
            return false;

        em.merge(book);
        return true;
    }
}
