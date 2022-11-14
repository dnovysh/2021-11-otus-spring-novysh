package ru.otus.services.storage;

import ru.otus.dto.ReviewAddDto;
import ru.otus.model.BookExt;
import ru.otus.model.Review;

import java.util.Optional;

public interface BookExtStorage {
    long countReview(String id);

    Optional<Review> findReview(String id, String reviewId);

    Optional<BookExt> findById(String id);

    void addReview(String id, ReviewAddDto reviewAddDto);

    void updateReview(String id, Review review);

    void deleteReview(String id, String reviewId);
}
