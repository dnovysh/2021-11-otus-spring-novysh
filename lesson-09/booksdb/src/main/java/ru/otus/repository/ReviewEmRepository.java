package ru.otus.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.entity.BookId;
import ru.otus.core.entity.Review;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewEmRepository implements ReviewRepository {

    @PersistenceContext
    private final EntityManager em;

    public ReviewEmRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("select count(r) FROM Review r", Long.class).getSingleResult();
    }

    @Override
    public Optional<Review> findById(Integer id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAll() {
        var graph = em.getEntityGraph("Review-bookId");
        return em.createQuery("select r from Review r", Review.class)
                .setHint("javax.persistence.loadgraph", graph)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> findAllByBookId(Integer bookId) {
        var bookIdObj = em.find(BookId.class, bookId);
        return em.createQuery(
                        "select r from Review r where r.bookId=:bookId order by r.reviewDate, r.id",
                        Review.class)
                .setParameter("bookId", bookIdObj)
                .getResultList();
    }

    @Override
    @Transactional()
    public Review save(Review review) {
        if (review.getId() == null) {
            em.persist(review);
            em.flush();
            return review;
        }

        return em.merge(review);
    }

    @Override
    @Transactional()
    public void deleteById(Integer id) {
        findById(id).ifPresent((review) -> {
            review.setDeleted(true);
        });
    }
}
