package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dto.ReviewAddDto;
import ru.otus.model.BookExt;
import ru.otus.model.Review;
import ru.otus.services.serializer.BaseSerializer;
import ru.otus.services.serializer.SerializerFactory;
import ru.otus.services.storage.BookExtStorage;

@ShellComponent
@ShellCommandGroup("Review Commands")
public class ReviewCommands {
    private final BookExtStorage bookExtStorage;
    private final BaseSerializer<BookExt> bookExtSerializer;
    private final BaseSerializer<Review> reviewSerializer;

    public ReviewCommands(BookExtStorage bookExtStorage,
                          SerializerFactory<BookExt> bookExtSerializerFactory,
                          SerializerFactory<Review> reviewSerializerFactory) {
        this.bookExtStorage = bookExtStorage;
        this.bookExtSerializer = bookExtSerializerFactory.getSerializer();
        this.reviewSerializer = reviewSerializerFactory.getSerializer();
    }

    @ShellMethod(value = "review count", key = {"r-count", "countReview"})
    public void countReview(String bookId) {
        System.out.println(bookExtStorage.countReview(bookId));
    }

    @ShellMethod(value = "get review by book id and review id", key = {"r-id", "getReviewById"})
    public void getReviewById(
            String bookId,
            String reviewId,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(bookExtStorage.findReview(bookId, reviewId)
                .map(r -> reviewSerializer.serialize(r, indent))
                .orElse("Review item not found")
        );
    }

    @ShellMethod(value = "get all review", key = {"r-all", "getAllReview"})
    public void getAllReview(
            String bookId,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(bookExtStorage.findById(bookId)
                .map(be -> bookExtSerializer.serialize(be, indent))
                .orElse("Reviews not found"));
    }

    @ShellMethod(value = "add review", key = {"r-add", "addReview"})
    public void addReview(
            String bookId,
            String text) {
        bookExtStorage.addReview(bookId, new ReviewAddDto(text));
    }

    @ShellMethod(value = "change review", key = {"r-change", "changeReview"})
    public void changeReview(
            String bookId,
            String reviewId,
            String text) {
        bookExtStorage.updateReview(bookId, new Review(reviewId, text));
    }

    @ShellMethod(value = "remove review", key = {"r-remove", "removeReview"})
    public void removeReview(
            String bookId,
            String reviewId) {
        bookExtStorage.deleteReview(bookId, reviewId);
    }
}
