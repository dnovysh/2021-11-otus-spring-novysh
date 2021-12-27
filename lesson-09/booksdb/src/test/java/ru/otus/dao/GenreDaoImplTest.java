package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Dao for working with genres should")
@JdbcTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {
    private static final int EXPECTED_GENRES_COUNT = 13;
    private static final Genre EXISTING_GENRE_WITHOUT_POPULATE_CHILDREN_LIST =
            new Genre("57.20", "Computer Science", "57", null);
    private static final String NON_EXISTENT_GENRE_ID = "57.20.90";

    @Autowired
    private GenreDaoImpl genreDao;

    @DisplayName("return the expected count of genres")
    @Test
    void shouldReturnExpectedGenreCount() {
        int actualGenreCount = genreDao.count();
        assertThat(actualGenreCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("return the expected genre by its id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre actualGenre = genreDao.getByIdWithoutPopulateChildrenList(
                EXISTING_GENRE_WITHOUT_POPULATE_CHILDREN_LIST.id());
        assertThat(actualGenre).usingRecursiveComparison()
                .isEqualTo(EXISTING_GENRE_WITHOUT_POPULATE_CHILDREN_LIST);
    }

    @DisplayName("return null for non-existent genre id")
    @Test
    void shouldReturnNullForNonExistentGenreId() {
        Genre actualGenre = genreDao.getByIdWithoutPopulateChildrenList(NON_EXISTENT_GENRE_ID);
        assertNull(actualGenre);
    }

    @DisplayName("return a list of all genres")
    @Test
    void shouldReturnAllGenresList() {
        List<Genre> expectedGenres = List.of(
                new Genre("08", "Artificial Intelligence", null, null),
                new Genre("57", "Science", null, null),
                new Genre("57.20", "Computer Science", "57", null),
                new Genre("57.20.03", "Algorithms", "57.20", null),
                new Genre("57.20.16", "Coding", "57.20", null),
                new Genre("57.20.53", "Programming", "57.20", null),
                new Genre("57.20.54", "Programming Languages", "57.20", null),
                new Genre("57.20.61", "Software", "57.20", null),
                new Genre("57.20.63", "Technical", "57.20", null),
                new Genre("57.20.21", "Computers", "57.20", null),
                new Genre("57.20.21.40", "Internet", "57.20.21", null),
                new Genre("57.20.21.37", "Hackers", "57.20.21", null),
                new Genre("57.64", "Technology", "57", null)
        );
        List<Genre> actualGenres = genreDao.getAllWithoutPopulateChildrenList();
        assertThat(actualGenres).hasSameElementsAs(expectedGenres);
    }

    @DisplayName("return the entire genre hierarchy starting at the given id")
    @Test
    void shouldReturnEntireGenreHierarchyStartWithId() {
        Genre expectedGenre = new Genre("57.20", "Computer Science", "57",
                List.of(
                        new Genre("57.20.03", "Algorithms", "57.20", null),
                        new Genre("57.20.16", "Coding", "57.20", null),
                        new Genre("57.20.21", "Computers", "57.20",
                                List.of(
                                        new Genre("57.20.21.37", "Hackers", "57.20.21", null),
                                        new Genre("57.20.21.40", "Internet", "57.20.21", null)
                                )),
                        new Genre("57.20.53", "Programming", "57.20", null),
                        new Genre("57.20.54", "Programming Languages", "57.20", null),
                        new Genre("57.20.61", "Software", "57.20", null),
                        new Genre("57.20.63", "Technical", "57.20", null)
                ));
        Genre actualGenre = genreDao.getEntireHierarchyStartWithId("57.20");
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("return null for hierarchy starting at the non-existent genre ids")
    @ParameterizedTest
    @ValueSource(strings = {"0", "57.2", NON_EXISTENT_GENRE_ID})
    void shouldReturnNullForHierarchyStartingAtNonExistentGenreId(String id) {
        Genre actualGenre = genreDao.getEntireHierarchyStartWithId(id);
        assertNull(actualGenre);
    }

    @DisplayName("return the entire genre hierarchy")
    @Test
    void shouldReturnEntireGenreHierarchyStartWithRoot() {
        List<Genre> expectedGenres = List.of(
                new Genre("08", "Artificial Intelligence", null, null),
                new Genre("57", "Science", null,
                        List.of(
                                new Genre("57.20", "Computer Science", "57",
                                        List.of(
                                                new Genre("57.20.03", "Algorithms", "57.20", null),
                                                new Genre("57.20.16", "Coding", "57.20", null),
                                                new Genre("57.20.21", "Computers", "57.20",
                                                        List.of(
                                                                new Genre("57.20.21.37", "Hackers", "57.20.21", null),
                                                                new Genre("57.20.21.40", "Internet", "57.20.21", null)
                                                        )),
                                                new Genre("57.20.53", "Programming", "57.20", null),
                                                new Genre("57.20.54", "Programming Languages", "57.20", null),
                                                new Genre("57.20.61", "Software", "57.20", null),
                                                new Genre("57.20.63", "Technical", "57.20", null)
                                        )),
                                new Genre("57.64", "Technology", "57", null)
                        ))
        );
        List<Genre> actualGenres = genreDao.getEntireHierarchyStartWithRoot();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }
}
