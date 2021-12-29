package ru.otus.core.abstraction;

import org.springframework.shell.standard.ShellOption;
import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorageUnitOfWork {

    public long count();

    Optional<Review> findById(Integer id);

    public List<Review> findAll();

    public List<Review> findAllByBookId(Integer bookId);

    Review create(Review review);

    Review update(ReviewUpdateDto reviewUpdateDto);

    public void deleteById(Integer id);
}
