package ru.otus.core.abstraction;

import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorageUnitOfWork {

    long count();

    Optional<Review> findById(Integer id);

    List<Review> findAll();

    List<Review> findAllByBookId(Integer bookId);

    Review create(Review review);

    Review update(ReviewUpdateDto reviewUpdateDto);

    void deleteById(Integer id);
}
