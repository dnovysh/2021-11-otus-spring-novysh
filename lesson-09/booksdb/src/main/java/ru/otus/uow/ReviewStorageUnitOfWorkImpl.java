package ru.otus.uow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.ReviewStorageUnitOfWork;
import ru.otus.core.entity.Review;
import ru.otus.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewStorageUnitOfWorkImpl implements ReviewStorageUnitOfWork {

    private final ReviewRepository reviewRepository;

    public ReviewStorageUnitOfWorkImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return reviewRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    @Override
    public Review create(Review review) {
        if (review == null) {
            throw new IllegalArgumentException(
                    "The review being created must not be null");
        }

        if (review.getId() != null) {
            throw new IllegalArgumentException(
                    "The identifier for the review being created must not be set");
        }

        return reviewRepository.save(review);
    }

    @Transactional
    @Override
    public Review update(Review review) {
        if (review == null) {
            throw new IllegalArgumentException(
                    "The review being updated must not be null");
        }

        if (review.getId() == null) {
            throw new IllegalArgumentException(
                    "The identifier of the review being updated must not be null");
        }

        return reviewRepository.save(review);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        reviewRepository.deleteById(id);
    }
}
