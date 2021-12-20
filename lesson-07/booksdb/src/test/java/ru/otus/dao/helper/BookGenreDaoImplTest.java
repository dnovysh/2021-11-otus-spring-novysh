package ru.otus.dao.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Helper DAO for linking books and genres")
@JdbcTest
@Import(BookGenreDaoImpl.class)
class BookGenreDaoImplTest {

    @Autowired
    private BookGenreDaoImpl bookGenreDao;

    static Stream<Arguments> existsArgumentsProvider() {
        return Stream.of(
                arguments(new BookGenre(529, "57.20.61"), true),
                arguments(new BookGenre(529, "57.20.54"), true),
                arguments(new BookGenre(757, "57.20.53"), true),
                arguments(new BookGenre(529, "57.20.53"), false),
                arguments(new BookGenre(100, "77.77.77"), false),
                arguments(new BookGenre(529, "08"), false)
        );
    }

    @DisplayName("exists - should return true for the existing relation and false for not")
    @ParameterizedTest
    @MethodSource("existsArgumentsProvider")
    void exists(BookGenre bookGenre, boolean expected) {
        boolean actual = bookGenreDao.exists(bookGenre);
        assertEquals(expected, actual);
    }
}
