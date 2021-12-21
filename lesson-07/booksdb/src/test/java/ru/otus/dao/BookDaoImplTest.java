package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.dao.helper.BookAuthorDaoImpl;
import ru.otus.dao.helper.BookGenreDaoImpl;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Dao for working with books should")
@JdbcTest
@Import({BookAuthorDaoImpl.class, BookGenreDaoImpl.class, BookDaoImpl.class})
class BookDaoImplTest {
    private static final int EXPECTED_BOOKS_COUNT = 3;
    private static final Book EXISTING_BOOK =
            new Book(529, "Kotlin in Action", 360, BigDecimal.valueOf(450, 2),
                    "9781620000000", Date.valueOf("2016-05-23"),
                    List.of(
                            new Author(289, "Dmitry", null, "Jemerov"),
                            new Author(333, "Svetlana", null, "Isakova")
                    ),
                    List.of(
                            new Genre("57.20.54", "Programming Languages", "57.20", null),
                            new Genre("57.20.61", "Software", "57.20", null),
                            new Genre("57.20.63", "Technical", "57.20", null),
                            new Genre("57.64", "Technology", "57", null)
                    ));
    private static final List<Book> EXPECTED_ALL_BOOK_LIST = List.of(
            new Book(253, "The Cambridge Handbook of Artificial Intelligence", 368,
                    BigDecimal.valueOf(410, 2), "9780520000000", Date.valueOf("2014-07-31"),
                    List.of(
                            new Author(163, "Nick", null, "Bostrom"),
                            new Author(310, "Eliezer", null, "Yudkowsky"),
                            new Author(983, "Keith", null, "Frankish"),
                            new Author(987, "William", "M.", "Ramsey")
                    ),
                    List.of(
                            new Genre("08", "Artificial Intelligence", null, null),
                            new Genre("57.64", "Technology", "57", null)
                    )),
            EXISTING_BOOK,
            new Book(757, "Java Performance: The Definitive Guide", 500,
                    BigDecimal.valueOf(438, 2),
                    "9781450000000", Date.valueOf("2014-05-22"),
                    List.of(
                            new Author(1042, "Scott", null, "Oaks")
                    ),
                    List.of(
                            new Genre("57.20.53", "Programming", "57.20", null),
                            new Genre("57.20.61", "Software", "57.20", null),
                            new Genre("57.20.63", "Technical", "57.20", null),
                            new Genre("57.64", "Technology", "57", null)
                    ))
    );

    private static final int NON_EXISTENT_BOOK_ID = 100;

    @Autowired
    private BookDaoImpl bookDao;

    @DisplayName("return the expected count of books")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBookCount = bookDao.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("return the expected book by its id")
    @Test
    void shouldReturnExpectedBookById() {
        Book actualBook = bookDao.getById(EXISTING_BOOK.id());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(EXISTING_BOOK);
    }

    @DisplayName("return null for non-existent book id")
    @Test
    void shouldReturnNullForNonExistentBookId() {
        Book actualBook = bookDao.getById(NON_EXISTENT_BOOK_ID);
        assertNull(actualBook);
    }

    @DisplayName("return a list of all books")
    @Test
    void shouldReturnAllBooksList() {
        List<Book> actualBooks = bookDao.getAll();
        assertThat(actualBooks).hasSameElementsAs(EXPECTED_ALL_BOOK_LIST);
    }
}
