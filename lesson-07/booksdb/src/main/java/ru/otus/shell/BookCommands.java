package ru.otus.shell;

import org.hibernate.validator.constraints.ISBN;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.dao.BookDao;
import ru.otus.dao.dto.BookInsertDto;
import ru.otus.domain.Book;
import ru.otus.service.BaseSerializer;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

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

    @ShellMethod(value = "get book by id command", key = {"b-id", "getBookById"})
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
            @Size(min = 1, max = 255) String title,
            @ShellOption(defaultValue = ShellOption.NULL) int totalPages,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DecimalMin(value = "0.0", inclusive = false)
            @DecimalMax(value = "5.0")
                    BigDecimal rating,
            @ShellOption(defaultValue = ShellOption.NULL)
            @ISBN
                    String isbn,
            @ShellOption(defaultValue = ShellOption.NULL)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String publishedDate) {
        int insertedBookId = bookDao.insert(new BookInsertDto(
                title, totalPages, rating, isbn, null /*Date.valueOf(publishedDate)*/
        ));
        Book insertedBook = bookDao.getById(insertedBookId);
        System.out.println(bookSerializer.serialize(insertedBook));
    }

}
