package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dto.BookUpdateDto;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.services.serializer.BaseSerializer;
import ru.otus.services.serializer.SerializerFactory;
import ru.otus.services.storage.BookStorage;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
@ShellCommandGroup("Book Commands")
public class BookCommands {
    private final BookStorage bookStorage;
    private final BaseSerializer<Book> bookSerializer;
    private final BaseSerializer<List<Book>> bookListSerializer;

    public BookCommands(BookStorage bookStorage,
                        SerializerFactory<Book> bookSerializerFactory,
                        SerializerFactory<List<Book>> bookListSerializerFactory) {
        this.bookStorage = bookStorage;
        this.bookSerializer = bookSerializerFactory.getSerializer();
        this.bookListSerializer = bookListSerializerFactory.getSerializer();
    }

    @ShellMethod(value = "get book count", key = {"b-count", "getBookCount"})
    public void getBookCount() {
        System.out.println(bookStorage.count());
    }

    @ShellMethod(value = "get book by id", key = {"b-id", "getBookById"})
    public void getBookById(
            String id,
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        System.out.println(bookStorage.findById(id)
                .map(b -> bookSerializer.serialize(b, indent))
                .orElse("Book not found")
        );
    }

    @ShellMethod(value = "get all book", key = {"b-all", "getAllBook"})
    public void getAllBook(
            @ShellOption(defaultValue = BaseSerializer.DEFAULT_INDENT) String indent) {
        List<Book> books = bookStorage.findAll();
        System.out.println(bookListSerializer.serialize(books, indent));
    }

    @ShellMethod(value = "add book", key = {"b-add", "addBook"})
    public void addBook(
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(value = "--totalPages", defaultValue = ShellOption.NULL)
                    Integer totalPages,
            @ShellOption(value = "--isbn", defaultValue = ShellOption.NULL)
                    String isbn,
            @ShellOption(value = "--publishedDate", defaultValue = ShellOption.NULL)
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
                    message = "Published date must be in ISO format: YYYY-MM-DD")
                    String publishedDate
    ) {
        Book insertedBook = bookStorage.create(Book.builder()
                .title(title)
                .totalPages(totalPages)
                .isbn(isbn)
                .publishedDate(Optional.ofNullable(publishedDate).map(LocalDate::parse).orElse(null))
                .authors(new ArrayList<>())
                .genres(new ArrayList<>())
                .build()
        );
        System.out.println("Book successfully added:");
        System.out.println(bookSerializer.serialize(insertedBook));
    }

    @ShellMethod(value = "change book information", key = {"b-change", "changeBookInfo"})
    public void changeBookInfo(
            String id,
            @Size(min = 1, max = 255)
                    String title,
            @ShellOption(value = "--totalPages", defaultValue = ShellOption.NULL)
                    Integer totalPages,
            @ShellOption(value = "--isbn", defaultValue = ShellOption.NULL)
                    String isbn,
            @ShellOption(value = "--publishedDate", defaultValue = ShellOption.NULL)
            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
                    message = "Published date must be in ISO format: YYYY-MM-DD")
                    String publishedDate
    ) {
        Book updatedBook = null;
        try {
            updatedBook = bookStorage.update(new BookUpdateDto(
                    id, title, totalPages, isbn,
                    Optional.ofNullable(publishedDate).map(LocalDate::parse).orElse(null)));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (updatedBook != null) {
            System.out.println("Updated book:");
            System.out.println(bookSerializer.serialize(updatedBook));
        } else {
            System.out.println("Book not found");
        }
    }

    @ShellMethod(value = "remove book by id", key = {"b-remove", "removeBook"})
    public void removeBookById(String id) {
        bookStorage.deleteById(id);
    }

    @ShellMethod(value = "restore book by id", key = {"b-restore", "restoreBook"})
    public void restoreBookById(String id) {
        bookStorage.restoreById(id);
    }

    @ShellMethod(value = "add author", key = {"b-a-add", "addAuthor"})
    public void addAuthor(
            String id,
            String firstName,
            @ShellOption(value = "--lastName", defaultValue = ShellOption.NULL)
                    String lastName
    ) {
        bookStorage.addAuthor(id, new Author(firstName, lastName));
    }

    @ShellMethod(value = "remove author", key = {"b-a-remove", "removeAuthor"})
    public void removeAuthor(
            String id,
            String firstName,
            @ShellOption(value = "--lastName", defaultValue = ShellOption.NULL)
                    String lastName
    ) {
        bookStorage.removeAuthor(id, new Author(firstName, lastName));
    }


}
