package ru.otus.repository;

import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    long count();

    Optional<Review> findById(Integer id);

    List<Review> findAll();

    List<Review> findAllByBookId(Integer bookId);

    Review save(Review review);

    void deleteById(Integer id);
}
