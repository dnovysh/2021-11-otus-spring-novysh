package ru.otus.core.dto;

import ru.otus.core.entity.Review;

import java.math.BigDecimal;

public record ReviewUpdateDto(Integer id, String title, String text, BigDecimal rating) {
    public Review applyToReview(Review review) {
        review.setUpdatableFields(title, text, rating);
        return review;
    }
}
