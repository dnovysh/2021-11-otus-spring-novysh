package ru.otus.services.storage;

import org.springframework.stereotype.Service;
import ru.otus.dto.ReviewAddDto;
import ru.otus.model.BookExt;
import ru.otus.model.Review;
import ru.otus.repositories.BookExtRepository;

import java.util.Optional;

@Service
public class BookExtStorageImpl implements BookExtStorage {

    private final BookExtRepository bookExtRepository;

    public BookExtStorageImpl(BookExtRepository bookExtRepository) {
        this.bookExtRepository = bookExtRepository;
    }

    @Override
    public long countReview(String id) {
        var countDto = bookExtRepository.countReview(id);
        return (countDto != null) ? countDto.getCount() : 0L;
    }

    @Override
    public Optional<Review> findReview(String id, String reviewId) {
        return bookExtRepository.findReview(id, reviewId);
    }

    @Override
    public Optional<BookExt> findById(String id) {
        return bookExtRepository.findById(id);
    }

    @Override
    public void addReview(String id, ReviewAddDto reviewAddDto) {
        bookExtRepository.addReview(id, reviewAddDto);
    }

    @Override
    public void updateReview(String id, Review review) {
        bookExtRepository.updateReview(id, review);
    }

    @Override
    public void deleteReview(String id, String reviewId) {
        bookExtRepository.deleteReview(id, reviewId);
    }
}
