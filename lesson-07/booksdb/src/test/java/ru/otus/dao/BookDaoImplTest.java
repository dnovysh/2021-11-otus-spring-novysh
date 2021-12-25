package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import ru.otus.dao.dto.BookInsertDto;
import ru.otus.dao.dto.BookUpdateDto;
import ru.otus.dao.helper.*;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao for working with books should")
@JdbcTest
@Import({BookAuthorDaoImpl.class,
        BookGenreDaoImpl.class,
        BookDaoImpl.class,
        AuthorDaoImpl.class,
        GenreDaoImpl.class
})
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
    @Autowired
    private BookAuthorDao bookAuthorDao;
    @Autowired
    private BookGenreDao bookGenreDao;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private GenreDao genreDao;

    @DisplayName("return the expected count of books")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBookCount = bookDao.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("insert book")
    @Test
    void shouldInsertBook() {
        BookInsertDto expected = new BookInsertDto(
                "Core Java, Volume I: Fundamentals, Twelfth Edition",
                944,
                BigDecimal.valueOf(460, 2),
                "9780137673629",
                Date.valueOf("2021-12-21")
        );
        int actualBookId = bookDao.insert(expected);
        Book actualBook = bookDao.getById(actualBookId);
        assertAll("Book",
                () -> assertThat(actualBook.id()).isGreaterThan(0),
                () -> assertEquals(expected.title(), actualBook.title()),
                () -> assertEquals(expected.totalPages(), actualBook.totalPages()),
                () -> assertEquals(expected.rating(), actualBook.rating()),
                () -> assertEquals(expected.isbn(), actualBook.isbn()),
                () -> assertEquals(expected.publishedDate(), actualBook.publishedDate()),
                () -> assertNotNull(actualBook.authors()),
                () -> assertEquals(0, actualBook.authors().size()),
                () -> assertNotNull(actualBook.genres()),
                () -> assertEquals(0, actualBook.genres().size())
        );
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

    @DisplayName("correctly update the book")
    @Test
    void shouldCorrectlyUpdateBook() {
        int bookId = 253;
        Book originalBook = bookDao.getById(bookId);
        var expectedTitle = "Foo Bar";
        var expectedTotalPages = 900;
        var expectedRating = BigDecimal.valueOf(250, 2);
        var expectedIsbn = "9780137673629";
        var expectedPublishedDate = Date.valueOf("1986-03-25");
        Book expectedBook = new Book(
                bookId, expectedTitle, expectedTotalPages, expectedRating, expectedIsbn,
                expectedPublishedDate, originalBook.authors(), originalBook.genres()
        );
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                bookId, expectedTitle, expectedTotalPages, expectedRating, expectedIsbn,
                expectedPublishedDate
        );
        boolean isOk = bookDao.update(bookUpdateDto);
        Book actualBook = bookDao.getById(bookId);
        assertTrue(isOk);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("correctly delete the book by its ID")
    @Test
    void shouldCorrectlyDeleteBook() {
        final int existingBookId = EXISTING_BOOK.id();
        assertNotNull(bookDao.getById(existingBookId));

        boolean isOk = bookDao.deleteById(existingBookId);

        assertTrue(isOk);
        assertNull(bookDao.getById(existingBookId));
    }

    @DisplayName("attach the author to the book by their IDs")
    @Test
    void shouldAttachAuthorToBookById() {
        int existingBookId = 757;
        int existingAuthorId = 987;
        assertFalse(bookAuthorDao.exists(new BookAuthor(existingBookId, existingAuthorId)));
        boolean isOk = bookDao.attachAuthorToBookById(existingBookId, existingAuthorId);
        assertTrue(isOk);
        assertTrue(bookAuthorDao.exists(new BookAuthor(existingBookId, existingAuthorId)));
    }

    @DisplayName("attach an author should raise DataIntegrityViolationException if the book does not exist")
    @Test
    void attachAuthorShouldRaiseExceptionIfBookDoesNotExist() {
        final int nonExistentBookId = 200;
        final int existingAuthorId = 987;
        assertNull(bookDao.getById(nonExistentBookId));
        assertNotNull(authorDao.getById(existingAuthorId));
        assertThatThrownBy(() -> bookDao.attachAuthorToBookById(nonExistentBookId, existingAuthorId))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("attach an author should raise DataIntegrityViolationException if the author does not exist")
    @Test
    void attachShouldRaiseExceptionIfAuthorDoesNotExist() {
        final int existingBookId = 757;
        final int nonExistentAuthorId = 200;
        assertNotNull(bookDao.getById(existingBookId));
        assertNull(authorDao.getById(nonExistentAuthorId));
        assertThatThrownBy(() -> bookDao.attachAuthorToBookById(existingBookId, nonExistentAuthorId))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("attach an author should raise DuplicateKeyException if the relationship already exists")
    @Test
    void attachAuthorShouldRaiseExceptionIfRelationshipAlreadyExists() {
        final int bookId = 529;
        final int authorId = 333;
        assertTrue(bookAuthorDao.exists(new BookAuthor(bookId, authorId)));
        assertThatThrownBy(() -> bookDao.attachAuthorToBookById(bookId, authorId))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("attach the genre to the book by their IDs")
    @Test
    void shouldAttachGenreToBookById() {
        int existingBookId = 757;
        String existingGenreId = "57.20.54";
        assertFalse(bookGenreDao.exists(new BookGenre(existingBookId, existingGenreId)));
        boolean isOk = bookDao.attachGenreToBookById(existingBookId, existingGenreId);
        assertTrue(isOk);
        assertTrue(bookGenreDao.exists(new BookGenre(existingBookId, existingGenreId)));
    }

    @DisplayName("attach an genre should raise DataIntegrityViolationException if the book does not exist")
    @Test
    void attachGenreShouldRaiseExceptionIfBookDoesNotExist() {
        final int nonExistentBookId = 200;
        final String existingGenreId = "57.20.54";
        assertNull(bookDao.getById(nonExistentBookId));
        assertNotNull(genreDao.getByIdWithoutPopulateChildrenList(existingGenreId));
        assertThatThrownBy(() -> bookDao.attachGenreToBookById(nonExistentBookId, existingGenreId))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("attach an genre should raise DataIntegrityViolationException if the genre does not exist")
    @Test
    void attachShouldRaiseExceptionIfGenreDoesNotExist() {
        final int existingBookId = 757;
        final String nonExistentGenreId = "05.20";
        assertNotNull(bookDao.getById(existingBookId));
        assertNull(genreDao.getByIdWithoutPopulateChildrenList(nonExistentGenreId));
        assertThatThrownBy(() -> bookDao.attachGenreToBookById(existingBookId, nonExistentGenreId))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("attach an genre should raise DuplicateKeyException if the relationship already exists")
    @Test
    void attachGenreShouldRaiseExceptionIfRelationshipAlreadyExists() {
        final int bookId = 529;
        final String genreId = "57.20.54";
        assertTrue(bookGenreDao.exists(new BookGenre(bookId, genreId)));
        assertThatThrownBy(() -> bookDao.attachGenreToBookById(bookId, genreId))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }
}
