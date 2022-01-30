package ru.otus.repositories;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

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
        val query = getQueryByIdDeletedFalse(bookId);
        val update = new Update().push("authors", author);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public void removeAuthor(String bookId, Author author) {
        val query = getQueryByIdDeletedFalse(bookId);
        val update = new Update().pull("authors", author);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public void addGenre(String bookId, Genre genre) {
        val query = getQueryByIdDeletedFalse(bookId);
        val update = new Update().push("genres", genre);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }

    @Override
    public void removeGenre(String bookId, Genre genre) {
        val query = getQueryByIdDeletedFalse(bookId);
        val update = new Update().pull("genres", genre);
        val Book = mongoTemplate.updateFirst(query, update, Book.class);
    }

    private Query getQueryByIdDeletedFalse(String id) {
        return Query
                .query(Criteria.where("_id").is(new ObjectId(id)))
                .addCriteria(Criteria.where("deleted").is(false));
    }
}
