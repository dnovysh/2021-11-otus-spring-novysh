package ru.otus.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.ReviewStorageService;
import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.BookId;
import ru.otus.core.entity.Review;
import ru.otus.repository.ReviewRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewStorageServiceImpl implements ReviewStorageService {

    private final ReviewRepository reviewRepository;

    public ReviewStorageServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public long count() {
        return reviewRepository.count();
    }

    @Override
    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findAllByBookId(Integer bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    @Override
    public List<Review> findAllByBookId(BookId bookId) {
        return this.findAllByBookId(bookId.getId());
    }

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
        var review = optionalReview.get();
        review.setTitle(reviewUpdateDto.title());
        review.setText(reviewUpdateDto.text());
        review.setRating(reviewUpdateDto.rating());
        return review;
    }

    @Override
    public void deleteById(Integer id) {
        reviewRepository.deleteById(id);
    }
}
