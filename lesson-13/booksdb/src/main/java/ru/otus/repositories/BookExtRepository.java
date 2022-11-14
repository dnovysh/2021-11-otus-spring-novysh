package ru.otus.repositories;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.dto.aggregation.CountDto;
import ru.otus.model.BookExt;
import ru.otus.model.Review;

import java.util.Optional;

public interface BookExtRepository
        extends MongoRepository<BookExt, String>, BookExtRepositoryCustom {

    @Aggregation(pipeline = {
            "{$match: { _id: ObjectId(?0)}}",
            "{$project: { _id : 0 , count: { $size:'$reviews' }}}"
    })
    CountDto countReview(String id);

    @Aggregation(pipeline = {
            "{$match: { _id: ObjectId(?0)}}",
            "{$project: { _id : 0 , reviews: { $filter: { input: '$reviews', as: 'item', " +
                    "cond: {$eq: ['$$item._id', ObjectId(?1)] }}}}}",
            "{$unwind: '$reviews'}",
            "{$project: {'_id': '$reviews._id', 'text': '$reviews.text'}}"
    })
    Optional<Review> findReview(String id, String reviewId);
}
