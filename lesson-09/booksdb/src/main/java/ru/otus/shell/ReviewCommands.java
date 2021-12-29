package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.core.abstraction.BaseSerializer;
import ru.otus.core.abstraction.ReviewStorageUnitOfWork;
import ru.otus.core.dto.ReviewUpdateDto;
import ru.otus.core.entity.Review;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Review Commands")
public class ReviewCommands {
    private final ReviewStorageUnitOfWork reviewStorage;
    private final BaseSerializer<Review> reviewSerializer;
    private final BaseSerializer<List<Review>> reviewListSerializer;

    public ReviewCommands(ReviewStorageUnitOfWork reviewStorage,
                          BaseSerializer<Review> reviewSerializer,
                          BaseSerializer<List<Review>> reviewListSerializer) {
        this.reviewStorage = reviewStorage;
        this.reviewSerializer = reviewSerializer;
        this.reviewListSerializer = reviewListSerializer;
    }

    @ShellMethod(value = "get review count command", key = {"r-count", "getReviewCount"})
    public void getReviewCount() {
        System.out.println(reviewStorage.count());
    }

    @ShellMethod(value = "get review by id command", key = {"r-id", "getReview"})
    public void getReviewById(
            int id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(reviewStorage.findById(id)
                .map(b -> reviewSerializer.serialize(b, indent))
                .orElse("Review not found")
        );
    }

    @ShellMethod(value = "get all review command", key = {"r-all", "getAllReview"})
    public void getAllReview(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Review> reviews = reviewStorage.findAll();
        System.out.println(reviewListSerializer.serialize(reviews, indent));
    }

    @ShellMethod(value = "get reviews by book ID command", key = {"r-book", "getReviewsByBookId"})
    public void getReviewsByBookId(
            @ShellOption(value = "--bookId")
                    Integer bookId,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT)
                    String indent) {
        List<Review> reviews = reviewStorage.findAllByBookId(bookId);
        System.out.println(reviewListSerializer.serialize(reviews, indent));
    }

    @ShellMethod(value = "add review command", key = {"r-add", "addReview"})
    public void addReview(
            @ShellOption(value = "--bookId")
                    Integer bookId,
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(defaultValue = ShellOption.NULL)
            @Size(max = 2000)
                    String text,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DecimalMin(value = "0.0", inclusive = false)
            @DecimalMax(value = "5.0")
                    BigDecimal rating
    ) {
        Review insertedReview = reviewStorage.create(new Review(title, text, rating, bookId));
        System.out.println("Review successfully added:");
        System.out.println(reviewSerializer.serialize(insertedReview));
    }

    @ShellMethod(value = "change review information command", key = {"r-change", "changeReviewInfo"})
    public void changeReviewInfo(
            Integer id,
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(defaultValue = ShellOption.NULL)
            @Size(max = 2000)
                    String text,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DecimalMin(value = "0.0", inclusive = false)
            @DecimalMax(value = "5.0")
                    BigDecimal rating
    ) {
        Review updatedReview = reviewStorage.update(new ReviewUpdateDto(
                id, title, text, rating));
        if (updatedReview != null) {
            System.out.println("Updated review:");
            System.out.println(reviewSerializer.serialize(updatedReview));
        } else {
            System.out.println("Review not found");
        }
    }

    @ShellMethod(value = "remove review by id command", key = {"r-remove", "removeReview"})
    public void removeReviewById(int id) {
        reviewStorage.deleteById(id);
    }
}
