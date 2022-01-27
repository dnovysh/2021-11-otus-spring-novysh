package ru.otus.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.otus.core.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Integer> {

    long count();

    Optional<Review> findById(Integer id);

    @EntityGraph(value = "Review-bookId")
    List<Review> findAll();

    List<Review> findByBookId_IdOrderByReviewDateAscIdAsc(Integer bookId);

    Review saveAndFlush(Review review);

    @Modifying
    @Query("update Review r set r.deleted = true where r.id = :id")
    void deleteById(@Param("id") Integer id);
}
