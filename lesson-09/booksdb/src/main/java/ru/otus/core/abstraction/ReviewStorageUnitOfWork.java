package ru.otus.core.abstraction;

import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorageUnitOfWork {

    public long count();

    Optional<Review> findById(Integer id);

    public List<Review> findAll();

    Review create(Review review);

    Review update(Review review);

    public void deleteById(Integer id);
}
