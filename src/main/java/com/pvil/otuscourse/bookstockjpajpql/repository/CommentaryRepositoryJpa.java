package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentaryRepositoryJpa implements CommentaryRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Commentary> getAllForBook(Book book) {
        TypedQuery<Commentary> query = em.createQuery("select c from Commentary c where c.book = :book", Commentary.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void add(Commentary commentary) {
        em.persist(commentary);
    }

    @Override
    @Transactional
    public boolean delete(Commentary commentary) {
        Commentary deleting = em.find(Commentary.class, commentary.getId());
        if (deleting == null)
            return false;

        em.remove(deleting);
        em.flush();
        return true;
    }
}
