package ru.otus.service.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.BookId;
import ru.otus.core.entity.Review;
import ru.otus.repository.ReviewEmRepository;
import ru.otus.service.storage.ReviewStorageServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for operations with reviews should")
@DataJpaTest
@Import({ReviewStorageServiceImpl.class, ReviewEmRepository.class})
class ReviewStorageServiceImplTest {
    private static final int EXPECTED_REVIEWS_COUNT = 3;
    private static final int NON_EXISTENT_REVIEW_ID = 50;
    private static final List<Review> ALL_EXISTING_REVIEWS =
            List.of(
                    new Review(103, new BookId(529),
                            "THE Kotlin book", null,
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2018-08-01"), false),
                    new Review(265, new BookId(529),
                            "Great introduction to Kotlin",
                            "We started using Kotlin in 2019 and I used this book to introduce the team to it...",
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2020-12-28"), false),
                    new Review(230, new BookId(529),
                            "Awesome reference",
                            "I really love this book, and expect to be referring back to it for many years to come.",
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2021-03-25"), false)
            );

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReviewStorageServiceImpl reviewStorage;

    @DisplayName("return expected count of reviews")
    @Test
    void shouldReturnExpectedReviewCount() {
        long actualReviewCount = reviewStorage.count();
        assertThat(actualReviewCount).isEqualTo(EXPECTED_REVIEWS_COUNT);
    }

    @DisplayName("return expected review by its id")
    @Test
    void shouldReturnExpectedReviewById() {
        var existingReview = ALL_EXISTING_REVIEWS.get(1);

        var actualReview = reviewStorage.findById(existingReview.getId());
        assertThat(actualReview)
                .isNotEmpty().get()
                .usingRecursiveComparison()
                .ignoringCollectionOrder().isEqualTo(existingReview);
    }

    @DisplayName("return optional empty for non-existent review id")
    @Test
    void shouldReturnEmptyForNonExistentReviewId() {
        var actualReview = reviewStorage.findById(NON_EXISTENT_REVIEW_ID);
        assertThat(actualReview).isEmpty();
    }

    @DisplayName("return a list of all reviews")
    @Test
    void shouldReturnAllReviews() {
        var actualReviews = reviewStorage.findAll();
        assertThat(actualReviews)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(ALL_EXISTING_REVIEWS);
    }

    @DisplayName("return a list of all reviews by book id")
    @Test
    void shouldReturnAllReviewsByBookId() {
        var bookId = em.find(BookId.class, 757);
        var expectedReviews = List.of(
                Review.builder().bookId(bookId).title("Foo")
                        .reviewDate(Date.valueOf("2021-08-21")).build(),
                Review.builder().bookId(bookId).title("Bar")
                        .reviewDate(Date.valueOf("2021-10-20")).build()
        );
        expectedReviews.forEach(em.getEntityManager()::persist);
        em.flush();
        expectedReviews.forEach(em::detach);
        var actualReviews = reviewStorage.findAllByBookId(bookId);
        assertThat(actualReviews)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedReviews);
    }

    @DisplayName("insert review correctly")
    @Test
    void shouldInsertReviewCorrectly() {
        final var bookId = em.find(BookId.class, 529);
        var title = "Best Book For Kotlin Developer";
        var newReview = new Review(bookId, title,
                "Best book for learning kotlin from the basic to advance.",
                BigDecimal.valueOf(500, 2));
        assertThat(em.getEntityManager()
                .createQuery("select count(r) from Review r where r.title = :title", Long.class)
                .setParameter("title", title)
                .getSingleResult()
        ).isZero();
        var createdReview = reviewStorage.create(newReview);
        assertThat(createdReview.getId()).isPositive();
        assertThat(createdReview.getTitle()).isEqualTo(title);
        em.detach(createdReview);
        var actualReviews = em.getEntityManager()
                .createQuery("select r from Review r where r.title = :title", Review.class)
                .setParameter("title", title)
                .getResultList();
        assertThat(actualReviews.size()).isEqualTo(1);
        var actualReview = actualReviews.get(0);
        assertThat(actualReview).isNotSameAs(createdReview);
        assertThat(actualReview).usingRecursiveComparison().isEqualTo(createdReview);
    }

    @DisplayName("update review correctly")
    @Test
    void shouldUpdateReviewCorrectly() {
        var existingReview = ALL_EXISTING_REVIEWS.get(0);
        var originalReview = em.find(Review.class, existingReview.getId());
        em.detach(originalReview);
        assertThat(originalReview)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(existingReview);
        var updateDto = new ReviewUpdateDto(
                existingReview.getId(),
                "Foo",
                "Bar",
                BigDecimal.valueOf(275, 2)
        );
        var expectedReview = new Review(
                updateDto.id(),
                existingReview.getBookId(),
                updateDto.title(),
                updateDto.text(),
                updateDto.rating(),
                existingReview.getReviewDate(),
                existingReview.isDeleted()
        );
        var updatedReview = reviewStorage.update(updateDto);
        em.flush();
        em.detach(updatedReview);
        var actualReview = em.find(Review.class, updateDto.id());
        assertThat(actualReview)
                .isNotNull()
                .isNotSameAs(originalReview)
                .isNotSameAs(updatedReview)
                .usingRecursiveComparison()
                .isEqualTo(updatedReview)
                .isEqualTo(expectedReview);
    }

    @DisplayName("delete review correctly")
    @Test
    void shouldDeleteReviewCorrectly() {
        var existingReviewId = ALL_EXISTING_REVIEWS.get(1).getId();
        var originalReview = em.find(Review.class, existingReviewId);
        assertThat(originalReview).isNotNull();
        assertThat(originalReview.isDeleted()).isFalse();
        em.detach(originalReview);
        reviewStorage.deleteById(existingReviewId);
        em.flush();
        em.clear();
        var actualReview = em.find(Review.class, existingReviewId);
        assertThat(actualReview).isNotNull();
        assertThat(actualReview.isDeleted()).isTrue();
    }
}
