package ru.otus.repositories;

import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.model.Author;
import ru.otus.model.Book;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteById(String id) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        val update = Update.update("deleted", true);
        mongoTemplate.updateMulti(query, update, Book.class);
    }

    @Override
    public void restoreById(String id) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        val update = Update.update("deleted", false);
        mongoTemplate.updateMulti(query, update, Book.class);
    }

    @Override
    public void addAuthor(String bookId, Author author) {
        val query = Query
                .query(Criteria.where("_id").is(new ObjectId(bookId)))
                .addCriteria(Criteria.where("deleted").is(false));
        val update = new Update().push("authors", author);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public void removeAuthor(String bookId, Author author) {
        val query = Query
                .query(Criteria.where("_id").is(new ObjectId(bookId)))
                .addCriteria(Criteria.where("deleted").is(false));
        val update = new Update().pull("authors", author);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }
}
