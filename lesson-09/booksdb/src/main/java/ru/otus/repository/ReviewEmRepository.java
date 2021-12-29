package ru.otus.repository;

import org.springframework.stereotype.Repository;
import ru.otus.core.entity.Review;

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
    public long count() {
        return em.createQuery("select count(r) FROM Review r", Long.class).getSingleResult();
    }

    @Override
    public Optional<Review> findById(Integer id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public List<Review> findAll() {
        return em.createQuery("select r from Review r", Review.class).getResultList();
    }

    @Override
    public List<Review> findAllByBookId(Integer bookId) {
        return em.createQuery(
                        "select r from Review r where r.bookId=:bookId order by r.reviewDate, r.id",
                        Review.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    @Override
    public Review save(Review review) {
        if (review.getId() == null) {
            em.persist(review);
            em.flush();
            return review;
        }

        return em.merge(review);
    }

    @Override
    public void deleteById(Integer id) {
        findById(id).ifPresent((review) -> {
            review.setDeleted(true);
        });
    }
}
