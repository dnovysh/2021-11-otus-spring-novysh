package ru.otus.shell;

import org.hibernate.validator.constraints.ISBN;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dao.BookDao;
import ru.otus.dao.dto.BookInsertDto;
import ru.otus.dao.dto.BookUpdateDto;
import ru.otus.domain.Book;
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
    private final BookDao bookDao;
    private final BaseSerializer<Book> bookSerializer;
    private final BaseSerializer<List<Book>> bookListSerializer;

    public BookCommands(BookDao bookDao,
                        BaseSerializer<Book> bookSerializer,
                        BaseSerializer<List<Book>> bookListSerializer) {
        this.bookDao = bookDao;
        this.bookSerializer = bookSerializer;
        this.bookListSerializer = bookListSerializer;
    }

    @ShellMethod(value = "get book count command", key = {"b-count", "getBookCount"})
    public void getBookCount() {
        System.out.println(bookDao.count());
    }

    @ShellMethod(value = "get book by id command", key = {"b-id", "getBook"})
    public void getBookById(
            int id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(bookSerializer.serialize(bookDao.getById(id), indent));
    }

    @ShellMethod(value = "get all book command", key = {"b-all", "getAllBook"})
    public void getAllBook(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Book> books = bookDao.getAll();
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
        int insertedBookId = bookDao.insert(new BookInsertDto(
                title, totalPages, rating, isbn,
                Optional.ofNullable(publishedDate).map(Date::valueOf).orElse(null)
        ));
        Book insertedBook = bookDao.getById(insertedBookId);
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
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                id, title, totalPages, rating, isbn,
                Optional.ofNullable(publishedDate).map(Date::valueOf).orElse(null));
        boolean isOk = bookDao.update(bookUpdateDto);
        if (isOk) {
            System.out.println("Book successfully changed:");
        } else {
            System.out.println("Unable to change book information, current information:");
        }
        Book updatedBook = bookDao.getById(bookUpdateDto.id());
        System.out.println(bookSerializer.serialize(updatedBook));
    }

    @ShellMethod(value = "remove book by id command", key = {"b-remove", "removeBook"})
    public void removeBookById(int id) {
        boolean isOk = bookDao.deleteById(id);
        if (isOk) {
            System.out.println("Book successfully deleted");
        } else {
            System.out.println("There is no book with the given ID");
        }
    }

    @ShellMethod(value = "assign an author to a book by their identifiers command",
            key = {"b-assign-author", "assignAuthor"})
    public void assignAuthor(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--authorId") int authorId) {
        boolean isOk = bookDao.attachAuthorToBookById(bookId, authorId);
        if (isOk) {
            System.out.println("Author successfully assigned");
        } else {
            System.out.println("There is no book or author with the given ID");
        }
    }

    @ShellMethod(value = "exclude an author from a book command",
            key = {"b-exclude-author", "excludeAuthor"})
    public void excludeAuthor(
            @ShellOption(value = "--bookId") int bookId,
            @ShellOption(value = "--authorId") int authorId) {
        boolean isOk = bookDao.detachAuthorFromBookById(bookId, authorId);
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
        boolean isOk = bookDao.attachGenreToBookById(bookId, genreId);
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
        boolean isOk = bookDao.detachGenreFromBookById(bookId, genreId);
        if (isOk) {
            System.out.println("Genre successfully excluded");
        } else {
            System.out.println(
                    "There is no book, no genre, nor their relationship with the given IDs");
        }
    }
}
