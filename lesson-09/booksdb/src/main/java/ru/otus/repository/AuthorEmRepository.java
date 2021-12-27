package ru.otus.repository;

import org.springframework.stereotype.Repository;
import ru.otus.core.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorEmRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    public AuthorEmRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public long count() {
        return em.createQuery("select count(a) FROM Author a", Long.class).getSingleResult();
    }

    @Override
    public Optional<Author> findById(Integer id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }
}
