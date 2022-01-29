package ru.otus.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.model.*;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.ReviewRepository;

import java.time.LocalDate;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Book kotlinInAction;
    private Book cryptonomicon;

    @ChangeSet(order = "000", id = "dropDB", author = "dnovysh", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "dnovysh", runAlways = true)
    public void initBooks(BookRepository repository) {
        kotlinInAction = repository.save(Book.builder()
                .title("Kotlin in Action")
                .totalPages(360)
                .isbn("9781620000000")
                .publishedDate(LocalDate.of(2016, 5, 23))
                .deleted(false)
                .authors(List.of(
                        new Author("Dmitry", "Jemerov"),
                        new Author("Svetlana", "Isakova")
                ))
                .genres(List.of(
                        new Genre("Programming"),
                        new Genre("Software"),
                        new Genre("Technical")
                ))
                .build());
        cryptonomicon = repository.save(Book.builder()
                .title("Cryptonomicon")
                .totalPages(1139)
                .isbn("9780060000000")
                .publishedDate(LocalDate.of(2002, 11, 1))
                .deleted(false)
                .authors(List.of(
                        new Author("Neal", "Stephenson")
                ))
                .genres(List.of(
                        new Genre("Fiction"),
                        new Genre("Cyberpunk")
                ))
                .build());
    }

    @ChangeSet(order = "002", id = "initReviews", author = "dnovysh", runAlways = true)
    public void initReviews(ReviewRepository repository) {
        repository.save(new Review(
                kotlinInAction.getId(),
                List.of(
                        new ReviewItem(ObjectId.get(), "Great introduction to Kotlin"),
                        new ReviewItem(ObjectId.get(), "Awesome reference"),
                        new ReviewItem(ObjectId.get(), "THE Kotlin book")
                )
        ));
        repository.save(new Review(
                cryptonomicon.getId(),
                List.of(
                        new ReviewItem(ObjectId.get(), "Excellent book")
                )
        ));
    }
}
