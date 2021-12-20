package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Dao for working with authors should")
@JdbcTest
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {
    private static final int EXPECTED_AUTHORS_COUNT = 8;
    private static final Author EXISTING_AUTHOR =
            new Author(1042, "Scott", null, "Oaks");
    private static final int NON_EXISTENT_AUTHOR_ID = 100;

    @Autowired
    private AuthorDaoImpl authorDao;

    @DisplayName("return the expected count of authors")
    @Test
    void shouldReturnExpectedAuthorCount() {
        int actualAuthorCount = authorDao.count();
        assertThat(actualAuthorCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("return the expected author by its id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author actualAuthor = authorDao.getById(EXISTING_AUTHOR.id());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR);
    }

    @DisplayName("return null for non-existent author id")
    @Test
    void shouldReturnNullForNonExistentAuthorId() {
        Author actualAuthor = authorDao.getById(NON_EXISTENT_AUTHOR_ID);
        assertNull(actualAuthor);
    }

    @DisplayName("return a list of all authors")
    @Test
    void shouldReturnAllAuthorsList() {
        List<Author> expectedAuthors = List.of(
                new Author(163, "Nick", null, "Bostrom"),
                new Author(289, "Dmitry", null, "Jemerov"),
                new Author(310, "Eliezer", null, "Yudkowsky"),
                new Author(333, "Svetlana", null, "Isakova"),
                new Author(983, "Keith", null, "Frankish"),
                new Author(987, "William", "M.", "Ramsey"),
                new Author(1042, "Scott", null, "Oaks"),
                new Author(1071, "Shoko", null, "Azuma")
        );
        List<Author> actualAuthors = authorDao.getAll();
        assertThat(actualAuthors).hasSameElementsAs(expectedAuthors);
    }
}
