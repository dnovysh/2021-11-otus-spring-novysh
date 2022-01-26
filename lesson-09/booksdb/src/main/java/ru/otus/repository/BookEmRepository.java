package ru.otus.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BookEmRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public BookEmRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("select count(b) FROM Book b", Long.class).getSingleResult();
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    @Transactional()
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            em.flush();
            return book;
        }

        return em.merge(book);
    }

    @Override
    @Transactional()
    public void deleteById(Integer id) {
        findById(id).ifPresent(em::remove);
    }
}
