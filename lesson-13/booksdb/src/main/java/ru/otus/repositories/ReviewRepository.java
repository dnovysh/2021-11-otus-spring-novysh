package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
