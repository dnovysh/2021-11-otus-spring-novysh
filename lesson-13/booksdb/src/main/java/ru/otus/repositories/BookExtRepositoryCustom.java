package ru.otus.repositories;

import ru.otus.dto.ReviewAddDto;
import ru.otus.model.Review;

public interface BookExtRepositoryCustom {
    void addReview(String id, ReviewAddDto reviewAddDto);

    void updateReview(String id, Review review);

    void deleteReview(String id, String reviewId);
}
