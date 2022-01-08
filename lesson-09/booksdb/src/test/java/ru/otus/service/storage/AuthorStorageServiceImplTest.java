package ru.otus.service.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.core.entity.Author;
import ru.otus.repository.AuthorEmRepository;
import ru.otus.service.storage.AuthorStorageServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for operations with authors should")
@DataJpaTest
@Import({AuthorStorageServiceImpl.class, AuthorEmRepository.class})
class AuthorStorageServiceImplTest {
    private static final int EXPECTED_AUTHORS_COUNT = 8;
    private static final Author EXISTING_AUTHOR =
            new Author(1042, "Scott", null, "Oaks");
    private static final int NON_EXISTENT_AUTHOR_ID = 100;

    @Autowired
    private AuthorStorageServiceImpl authorStorage;

    @DisplayName("return the expected count of authors")
    @Test
    void shouldReturnExpectedAuthorCount() {
        long actualAuthorCount = authorStorage.count();
        assertThat(actualAuthorCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("return the expected author by its id")
    @Test
    void shouldReturnExpectedAuthorById() {
        var actualAuthor = authorStorage.findById(EXISTING_AUTHOR.getId());
        assertThat(actualAuthor)
                .isNotEmpty().get()
                .usingRecursiveComparison().isEqualTo(EXISTING_AUTHOR);
    }

    @DisplayName("return optional empty for non-existent author id")
    @Test
    void shouldReturnNullForNonExistentAuthorId() {
        var actualAuthor = authorStorage.findById(NON_EXISTENT_AUTHOR_ID);
        assertThat(actualAuthor).isEmpty();
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
        List<Author> actualAuthors = authorStorage.findAll();
        assertThat(actualAuthors).hasSameElementsAs(expectedAuthors);
    }
}
