package ru.otus.repositories;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.dto.ReviewAddDto;
import ru.otus.model.BookExt;
import ru.otus.model.Review;
import ru.otus.utils.ObjectIdGenerator;

import java.util.List;

@RequiredArgsConstructor
public class BookExtRepositoryCustomImpl implements BookExtRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private final ObjectIdGenerator objectIdGenerator;
    Logger logger = LoggerFactory.getLogger(BookExtRepositoryCustomImpl.class);

    @Override
    public void addReview(String id, ReviewAddDto reviewAddDto) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        val review = new Review(objectIdGenerator.getObjectId(), reviewAddDto.text());
        val exists = mongoTemplate.exists(query, BookExt.class);
        if (!exists) {
            val bookExt = new BookExt(id, List.of(review));
            try {
                mongoTemplate.insert(bookExt);
                return;
            } catch (DuplicateKeyException e) {
                logger.info("BookExt already exists", e);
            }
        }
        val update = new Update().push("reviews", review);
        mongoTemplate.updateFirst(query, update, BookExt.class);
    }

    @Override
    public void updateReview(String id, Review review) {
        val query = Query
                .query(Criteria.where("_id").is(new ObjectId(id)))
                .addCriteria(Criteria.where("reviews._id").is(new ObjectId(review.getId())));
        val update = new Update().set("reviews.$", review);
        mongoTemplate.updateFirst(query, update, BookExt.class);
    }

    @Override
    public void deleteReview(String id, String reviewId) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        val update = new Update().pull("reviews",
                Query.query(Criteria.where("_id").is(new ObjectId(reviewId))));
        mongoTemplate.updateFirst(query, update, BookExt.class);
    }
}
