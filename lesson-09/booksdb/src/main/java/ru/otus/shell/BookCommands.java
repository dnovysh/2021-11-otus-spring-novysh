package ru.otus.shell;

import org.hibernate.validator.constraints.ISBN;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.core.abstraction.BookStorageUnitOfWork;
import ru.otus.core.entity.Book;
import ru.otus.dao.dto.BookUpdateDto;

import ru.otus.core.abstraction.BaseSerializer;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@ShellComponent
@ShellCommandGroup("Book Commands")
public class BookCommands {
    private final BookStorageUnitOfWork bookStorage;
    private final BaseSerializer<Book> bookSerializer;
    private final BaseSerializer<List<Book>> bookListSerializer;

    public BookCommands(BookStorageUnitOfWork bookStorage,
                        BaseSerializer<Book> bookSerializer,
                        BaseSerializer<List<Book>> bookListSerializer) {
        this.bookStorage = bookStorage;
        this.bookSerializer = bookSerializer;
        this.bookListSerializer = bookListSerializer;
    }

    @ShellMethod(value = "get book count command", key = {"b-count", "getBookCount"})
    public void getBookCount() {
        System.out.println(bookStorage.count());
    }

    @ShellMethod(value = "get book by id command", key = {"b-id", "getBook"})
    public void getBookById(
            int id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(bookStorage.findById(id)
                .map(b -> bookSerializer.serialize(b, indent))
                .orElse("Book not found")
        );
    }

    @ShellMethod(value = "get all book command", key = {"b-all", "getAllBook"})
    public void getAllBook(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Book> books = bookStorage.findAll();
        System.out.println(bookListSerializer.serialize(books, indent));
    }

    @ShellMethod(value = "add book command", key = {"b-add", "addBook"})
    public void addBook(
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(value = "--totalPages", defaultValue = ShellOption.NULL)
                    Integer totalPages,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DecimalMin(value = "0.0", inclusive = false)
            @DecimalMax(value = "5.0")
                    BigDecimal rating,
            @ShellOption(defaultValue = ShellOption.NULL)
            @ISBN
                    String isbn,
            @ShellOption(value = "--publishedDate", defaultValue = ShellOption.NULL)
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
                    message = "Published date must be in ISO format: YYYY-MM-DD")
                    String publishedDate
    ) {
        Book insertedBook = bookStorage.create(new Book(
                title, totalPages, rating, isbn,
                Optional.ofNullable(publishedDate).map(Date::valueOf).orElse(null)
        ));
        System.out.println("Book successfully added:");
        System.out.println(bookSerializer.serialize(insertedBook));
    }

    @ShellMethod(value = "change book information command", key = {"b-change", "changeBookInfo"})
    public void changeBookInfo(
            int id,
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(value = "--totalPages", defaultValue = ShellOption.NULL)
                    Integer totalPages,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DecimalMin(value = "0.0", inclusive = false)
            @DecimalMax(value = "5.0")
                    BigDecimal rating,
            @ShellOption(defaultValue = ShellOption.NULL)
            @ISBN
                    String isbn,
            @ShellOption(value = "--publishedDate", defaultValue = ShellOption.NULL)
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
                    message = "Published date must be in ISO format: YYYY-MM-DD")
                    String publishedDate
    ) {
        Book updatedBook = new Book(
                id, title, totalPages, rating, isbn,
                Optional.ofNullable(publishedDate).map(Date::valueOf).orElse(null));
        System.out.println("Updated book:");
        System.out.println(bookSerializer.serialize(updatedBook));
    }

    @ShellMethod(value = "remove book by id command", key = {"b-remove", "removeBook"})
    public void removeBookById(int id) {
        bookStorage.deleteById(id);
    }

    @ShellMethod(value = "assign an author to a book by their identifiers command",
            key = {"b-assign-author", "assignAuthor"})
    public void assignAuthor(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--authorId") int authorId) {
        Optional<Book> optionalBook = bookStorage.addAuthorToBookById(bookId, authorId);
        if (optionalBook.isPresent()) {
            System.out.println("Author successfully assigned");
            System.out.println(optionalBook.map(bookSerializer::serialize));
        } else {
            System.out.println("There is no book or author with the given ID");
        }
    }

    @ShellMethod(value = "exclude an author from a book command",
            key = {"b-exclude-author", "excludeAuthor"})
    public void excludeAuthor(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--authorId") int authorId) {
        boolean isOk = bookStorage.detachAuthorFromBookById(bookId, authorId);
        if (isOk) {
            System.out.println("Author successfully excluded");
        } else {
            System.out.println(
                    "There is no book, no author, nor their relationship with the given IDs");
        }
    }

    @ShellMethod(value = "assign an genre to a book by their identifiers command",
            key = {"b-assign-genre", "assignGenre"})
    public void assignGenre(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--genreId") String genreId) {
        boolean isOk = bookStorage.attachGenreToBookById(bookId, genreId);
        if (isOk) {
            System.out.println("Genre successfully assigned");
        } else {
            System.out.println("There is no book or genre with the given ID");
        }
    }

    @ShellMethod(value = "exclude an genre from a book command",
            key = {"b-exclude-genre", "excludeGenre"})
    public void excludeGenre(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--genreId") String genreId) {
        boolean isOk = bookStorage.detachGenreFromBookById(bookId, genreId);
        if (isOk) {
            System.out.println("Genre successfully excluded");
        } else {
            System.out.println(
                    "There is no book, no genre, nor their relationship with the given IDs");
        }
    }
}
