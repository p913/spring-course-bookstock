package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getByName(String filter) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name like :filter", Genre.class);
        query.setParameter("filter", "%" + filter + "%");
        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(Genre genre) {
        em.persist(genre);
    }

    @Override
    @Transactional
    public boolean delete(Genre genre) {
        Genre deleting = em.find(Genre.class, genre.getId());
        if (deleting == null)
            return false;

        em.remove(deleting);
        em.flush();
        return true;
    }

    @Override
    @Transactional
    public boolean update(Genre genre) {
        if (em.find(Genre.class, genre.getId()) == null)
            return false;

        em.merge(genre);
        return true;
    }
}
