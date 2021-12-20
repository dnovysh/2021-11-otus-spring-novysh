package ru.otus.dao.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import ru.otus.domain.Author;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("The helper DAO for linking books and authors")
@JdbcTest
@Import(BookAuthorDaoImpl.class)
class BookAuthorDaoImplTest {

    @Autowired
    private BookAuthorDaoImpl bookAuthorDao;

    static Stream<Arguments> deleteArgumentsProvider() {
        return Stream.of(
                arguments(new BookAuthor(529, 333), true),
                arguments(new BookAuthor(100, 200), false)
        );
    }

    static Stream<Arguments> existsArgumentsProvider() {
        return Stream.of(
                arguments(new BookAuthor(529, 333), true),
                arguments(new BookAuthor(100, 200), false),
                arguments(new BookAuthor(757, 987), false)
        );
    }

    @DisplayName("getAllByBookId - should return the expected list of authors")
    @Test
    void getAllByBookIdShouldReturnExpectedAuthorList() {
        List<Author> expectedAuthors = List.of(
                new Author(289, "Dmitry", null, "Jemerov"),
                new Author(333, "Svetlana", null, "Isakova")
        );
        List<Author> actualAuthors = bookAuthorDao.getAllByBookId(529);
        assertThat(actualAuthors).hasSameElementsAs(expectedAuthors);
    }

    @DisplayName("getAll - should return the expected map between book ids and lists of authors")
    @Test
    void getAllShouldReturnExpectedMapBookIdAuthorLists() {
        Map<Integer, List<Author>> expectedAuthors = Map.of(
                253, List.of(
                        new Author(163, "Nick", null, "Bostrom"),
                        new Author(310, "Eliezer", null, "Yudkowsky"),
                        new Author(983, "Keith", null, "Frankish"),
                        new Author(987, "William", "M.", "Ramsey")),
                529, List.of(
                        new Author(289, "Dmitry", null, "Jemerov"),
                        new Author(333, "Svetlana", null, "Isakova")),
                757, List.of(new Author(1042, "Scott", null, "Oaks"))
        );
        Map<Integer, List<Author>> actualAuthors = bookAuthorDao.getAll();
        assertThat(actualAuthors).usingRecursiveComparison().isEqualTo(expectedAuthors);
    }

    @DisplayName("insert - should create a new relation between the book and the author")
    @Test
    void insert() {
        var bookAuthor = new BookAuthor(757, 987);
        assertFalse(bookAuthorDao.exists(bookAuthor));
        bookAuthorDao.insert(bookAuthor);
        assertTrue(bookAuthorDao.exists(bookAuthor));
    }

    @DisplayName("insert - should raise an exception if the book or author is missing")
    @Test
    void insertMissingBookOrAuthor() {
        assertThatCode(() -> bookAuthorDao.insert(new BookAuthor(100, 987)))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatCode(() -> bookAuthorDao.insert(new BookAuthor(757, 200)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("insert - should raise an exception if the relation already exists")
    @Test
    void insertAlreadyExistsRelation() {
        assertThatCode(() -> bookAuthorDao.insert(new BookAuthor(757, 1042)))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("delete - should return true if successful and false if no relationship is found")
    @ParameterizedTest
    @MethodSource("deleteArgumentsProvider")
    void delete(BookAuthor bookAuthor, boolean expected) {
        boolean actual = bookAuthorDao.delete(bookAuthor);
        assertEquals(expected, actual);
    }

    @DisplayName("exists - should return true for the existing relation and false for not")
    @ParameterizedTest
    @MethodSource("existsArgumentsProvider")
    void exists(BookAuthor bookAuthor, boolean expected) {
        boolean actual = bookAuthorDao.exists(bookAuthor);
        assertEquals(expected, actual);
    }
}
