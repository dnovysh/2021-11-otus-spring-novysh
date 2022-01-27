package ru.otus.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.otus.core.entity.Review;

import java.sql.Date;
import java.util.List;

public record BookReviewsViewDto(
        Integer id,
        String title,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Date published_date,
        List<Review> reviews
) {
}
