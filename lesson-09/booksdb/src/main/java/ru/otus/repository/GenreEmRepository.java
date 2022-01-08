package ru.otus.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;

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
    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("select count(g) FROM Genre g", Long.class).getSingleResult();
    }

    @Override
    public Optional<Genre> findById(String id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<GenreClassifierView> getGenreClassifierStartWithId(String id) {
        return Optional.ofNullable(em.find(GenreClassifierView.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreClassifierView> getAllGenreClassifier() {
        return em.createQuery(
                "select g from GenreClassifierView g where g.parent is null order by g.id",
                GenreClassifierView.class).getResultList();
    }

    @Override
    public Optional<GenreParentsView> getGenreParentsById(String id) {
        return Optional.ofNullable(em.find(GenreParentsView.class, id));
    }
}
