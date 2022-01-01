package ru.otus.uow;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.ReviewStorageUnitOfWork;
import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.Review;
import ru.otus.repository.ReviewRepository;

import java.sql.Date;
import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    @Override
    public List<Review> findAllByBookId(Integer bookId) {
        return reviewRepository.findAllByBookId(bookId);
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
        review.setReviewDate(Date.valueOf(LocalDate.now()));
        review.setDeleted(false);
        return reviewRepository.save(review);
    }

    @Transactional
    @Override
    public Review update(ReviewUpdateDto reviewUpdateDto) {
        if (reviewUpdateDto == null) {
            throw new IllegalArgumentException(
                    "The review being updated must not be null");
        }
        if (reviewUpdateDto.id() == null) {
            throw new IllegalArgumentException(
                    "The identifier of the review being updated must not be null");
        }
        var optionalReview = reviewRepository.findById(reviewUpdateDto.id());
        if (optionalReview.isEmpty()) {
            return null;
        }
        return optionalReview.map(reviewUpdateDto::applyToReview).get();
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        reviewRepository.deleteById(id);
    }
}
