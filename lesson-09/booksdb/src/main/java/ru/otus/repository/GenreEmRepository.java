package ru.otus.repository;

import org.springframework.stereotype.Repository;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreEmRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    public GenreEmRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public long count() {
        return em.createQuery("select count(g) FROM Genre g", Long.class).getSingleResult();
    }

    @Override
    public Optional<Genre> findById(String id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<GenreClassifier> getGenreClassifierStartWithId(String id) {
        return Optional.ofNullable(em.find(GenreClassifier.class, id));
    }

    @Override
    public List<GenreClassifier> getGenreClassifier() {
        return em.createQuery("select g from Genre g where g.parentId is null",
                GenreClassifier.class).getResultList();
    }
}
