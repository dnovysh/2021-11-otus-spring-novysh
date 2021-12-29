package ru.otus.repository;

import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    public long count();

    Optional<Review> findById(Integer id);

    public List<Review> findAll();

    List<Review> findAllByBookId(Integer bookId);

    Review save(Review review);

    public void deleteById(Integer id);
}
