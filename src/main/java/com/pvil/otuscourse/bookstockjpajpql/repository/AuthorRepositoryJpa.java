package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getByName(String filter) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name like :filter", Author.class);
        query.setParameter("filter", "%" + filter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(Author author) {
        em.persist(author);
    }

    @Override
    @Transactional
    public boolean delete(Author author) {
        Author deleting = em.find(Author.class, author.getId());
        if (deleting == null)
            return false;

        em.remove(deleting);
        em.flush();
        return true;
    }

    @Override
    @Transactional
    public boolean update(Author author) {
        if (em.find(Author.class, author.getId()) == null)
            return false;

        em.merge(author);
        return true;
    }
}
