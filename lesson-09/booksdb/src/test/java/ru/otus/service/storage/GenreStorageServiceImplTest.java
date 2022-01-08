package ru.otus.service.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.GenreClassifierView;
import ru.otus.core.entity.GenreParentsView;
import ru.otus.repository.GenreEmRepository;
import ru.otus.service.storage.GenreStorageServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for operations with genres should")
@DataJpaTest
@Import({GenreStorageServiceImpl.class, GenreEmRepository.class})
class GenreStorageServiceImplTest {
    private static final int EXPECTED_GENRES_COUNT = 13;
    private static final Genre EXISTING_GENRE =
            new Genre("57.20", "Computer Science");
    private static final String NON_EXISTENT_GENRE_ID = "57.20.90";

    @Autowired
    private GenreStorageServiceImpl genreStorage;

    @DisplayName("return the expected count of genres")
    @Test
    void shouldReturnExpectedGenreCount() {
        long actualGenreCount = genreStorage.count();
        assertThat(actualGenreCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("return the expected genre by its id")
    @Test
    void shouldReturnExpectedGenreById() {
        var actualGenre = genreStorage.findById(EXISTING_GENRE.getId());
        assertThat(actualGenre)
                .isNotEmpty().get()
                .usingRecursiveComparison().isEqualTo(EXISTING_GENRE);
    }

    @DisplayName("return optional empty for non-existent genre id")
    @Test
    void shouldReturnNullForNonExistentGenreId() {
        var actualGenre = genreStorage.findById(NON_EXISTENT_GENRE_ID);
        assertThat(actualGenre).isEmpty();
    }

    @DisplayName("return a list of all genres")
    @Test
    void shouldReturnAllGenresList() {
        List<Genre> expectedGenres = List.of(
                new Genre("08", "Artificial Intelligence"),
                new Genre("57", "Science"),
                new Genre("57.20", "Computer Science"),
                new Genre("57.20.03", "Algorithms"),
                new Genre("57.20.16", "Coding"),
                new Genre("57.20.53", "Programming"),
                new Genre("57.20.54", "Programming Languages"),
                new Genre("57.20.61", "Software"),
                new Genre("57.20.63", "Technical"),
                new Genre("57.20.21", "Computers"),
                new Genre("57.20.21.40", "Internet"),
                new Genre("57.20.21.37", "Hackers"),
                new Genre("57.64", "Technology")
        );
        List<Genre> actualGenres = genreStorage.findAll();
        assertThat(actualGenres).hasSameElementsAs(expectedGenres);
    }

    @DisplayName("return the entire genre hierarchy starting at the given id")
    @Test
    void shouldReturnEntireGenreHierarchyStartWithId() {
        var genre57 = new Genre("57", "Science");
        var genre5720 = new Genre("57.20", "Computer Science");
        var genre572021 = new Genre("57.20.21", "Computers");
        var emptyList = new ArrayList<GenreClassifierView>();
        var expectedGenre = new GenreClassifierView("57.20", "Computer Science", genre57,
                List.of(
                        new GenreClassifierView("57.20.03", "Algorithms", genre5720, emptyList),
                        new GenreClassifierView("57.20.16", "Coding", genre5720, emptyList),
                        new GenreClassifierView("57.20.21", "Computers", genre5720,
                                List.of(
                                        new GenreClassifierView("57.20.21.37", "Hackers",
                                                genre572021, emptyList),
                                        new GenreClassifierView("57.20.21.40", "Internet",
                                                genre572021, emptyList)
                                )),
                        new GenreClassifierView("57.20.53", "Programming", genre5720, emptyList),
                        new GenreClassifierView("57.20.54", "Programming Languages", genre5720, emptyList),
                        new GenreClassifierView("57.20.61", "Software", genre5720, emptyList),
                        new GenreClassifierView("57.20.63", "Technical", genre5720, emptyList)
                ));
        var actualGenre = genreStorage.getGenreClassifierStartWithId("57.20");
        assertThat(actualGenre)
                .isNotEmpty().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("return optional empty for hierarchy starting at the non-existent genre ids")
    @ParameterizedTest
    @ValueSource(strings = {"0", "57.2", NON_EXISTENT_GENRE_ID})
    void shouldReturnNullForHierarchyStartingAtNonExistentGenreId(String id) {
        var actualGenre = genreStorage.getGenreClassifierStartWithId(id);
        assertThat(actualGenre).isEmpty();
    }

    @DisplayName("return the entire genre hierarchy")
    @Test
    void shouldReturnEntireGenreHierarchyStartWithRoot() {
        var genre57 = new Genre("57", "Science");
        var genre5720 = new Genre("57.20", "Computer Science");
        var genre572021 = new Genre("57.20.21", "Computers");
        var emptyList = new ArrayList<GenreClassifierView>();
        var expectedGenres = List.of(
                new GenreClassifierView("08", "Artificial Intelligence", null, emptyList),
                new GenreClassifierView("57", "Science", null,
                        List.of(
                                new GenreClassifierView("57.20", "Computer Science", genre57,
                                        List.of(
                                                new GenreClassifierView("57.20.03", "Algorithms", genre5720, emptyList),
                                                new GenreClassifierView("57.20.16", "Coding", genre5720, emptyList),
                                                new GenreClassifierView("57.20.21", "Computers", genre5720,
                                                        List.of(
                                                                new GenreClassifierView("57.20.21.37", "Hackers",
                                                                        genre572021, emptyList),
                                                                new GenreClassifierView("57.20.21.40", "Internet",
                                                                        genre572021, emptyList)
                                                        )),
                                                new GenreClassifierView("57.20.53", "Programming", genre5720, emptyList),
                                                new GenreClassifierView("57.20.54", "Programming Languages", genre5720, emptyList),
                                                new GenreClassifierView("57.20.61", "Software", genre5720, emptyList),
                                                new GenreClassifierView("57.20.63", "Technical", genre5720, emptyList)
                                        )),
                                new GenreClassifierView("57.64", "Technology", genre57, emptyList)
                        ))
        );
        var actualGenres = genreStorage.getAllGenreClassifier();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @DisplayName("return the path by id")
    @Test
    void shouldReturnPathById() {
        var genre57 = new GenreParentsView("57", "Science", null, null);
        var genre5720 = new GenreParentsView("57.20", "Computer Science", genre57, null);
        var genre572021 = new GenreParentsView("57.20.21", "Computers", genre5720, null);
        genre57.setChild(genre5720);
        genre5720.setChild(genre572021);
        var actualPath = genreStorage.getGenrePathById("57.20.21");
        assertThat(actualPath)
                .isNotEmpty().get()
                .usingRecursiveComparison().isEqualTo(genre57);
    }
}
