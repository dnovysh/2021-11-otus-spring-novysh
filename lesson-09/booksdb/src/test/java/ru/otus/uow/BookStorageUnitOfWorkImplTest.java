package ru.otus.uow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.core.entity.Author;
import ru.otus.core.entity.Book;
import ru.otus.core.entity.Genre;
import ru.otus.core.entity.Review;

import ru.otus.repository.AuthorEmRepository;
import ru.otus.repository.BookEmRepository;
import ru.otus.repository.GenreEmRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.common.NormalizedString.toArray;

@DisplayName("Unit of work for operations with books should")
@DataJpaTest
@Import({BookStorageUnitOfWorkImpl.class,
        BookEmRepository.class,
        AuthorEmRepository.class,
        GenreEmRepository.class,
        AuthorStorageUnitOfWorkImpl.class,
        GenreStorageUnitOfWorkImpl.class})
class BookStorageUnitOfWorkImplTest {
    private static final int EXPECTED_BOOKS_COUNT = 3;
    // reviews field (lazy) is not populated
    private static final Book EXISTING_BOOK =
            new Book(529, "Kotlin in Action", 360, BigDecimal.valueOf(450, 2),
                    "9781620000000", Date.valueOf("2016-05-23"),
                    Set.of(
                            new Author(289, "Dmitry", null, "Jemerov"),
                            new Author(333, "Svetlana", null, "Isakova")
                    ),
                    Set.of(
                            new Genre("57.20.54", "Programming Languages", "57.20"),
                            new Genre("57.20.61", "Software", "57.20"),
                            new Genre("57.20.63", "Technical", "57.20"),
                            new Genre("57.64", "Technology", "57")
                    ),
                    new ArrayList<Review>());
    private static final List<Book> EXPECTED_ALL_BOOK_LIST = List.of(
            new Book(253, "The Cambridge Handbook of Artificial Intelligence", 368,
                    BigDecimal.valueOf(410, 2), "9780520000000", Date.valueOf("2014-07-31"),
                    Set.of(
                            new Author(163, "Nick", null, "Bostrom"),
                            new Author(310, "Eliezer", null, "Yudkowsky"),
                            new Author(983, "Keith", null, "Frankish"),
                            new Author(987, "William", "M.", "Ramsey")
                    ),
                    Set.of(
                            new Genre("08", "Artificial Intelligence", null),
                            new Genre("57.64", "Technology", "57")
                    ), new ArrayList<Review>()),
            EXISTING_BOOK,
            new Book(757, "Java Performance: The Definitive Guide", 500,
                    BigDecimal.valueOf(438, 2),
                    "9781450000000", Date.valueOf("2014-05-22"),
                    Set.of(
                            new Author(1042, "Scott", null, "Oaks")
                    ),
                    Set.of(
                            new Genre("57.20.53", "Programming", "57.20"),
                            new Genre("57.20.61", "Software", "57.20"),
                            new Genre("57.20.63", "Technical", "57.20"),
                            new Genre("57.64", "Technology", "57")
                    ), new ArrayList<Review>())
    );

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookStorageUnitOfWorkImpl bookStorage;

    @DisplayName("return the expected count of books")
    @Test
    void shouldReturnExpectedBookCount() {
        long actualBookCount = bookStorage.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("insert book correctly")
    @Test
    void shouldInsertBook() {
        final var isbn = "9780137673629";
        assertThat(em.getEntityManager()
                .createQuery("select count(b) from Book b where b.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult()
        ).isZero();

        var newAuthor = em.persistAndFlush(new Author(
                null, "Cay", null, "Horstmann"));
        var genre = em.find(Genre.class, "57.20.54");
        var newBook = new Book(
                null,
                "Core Java, Volume I: Fundamentals, Twelfth Edition",
                944,
                BigDecimal.valueOf(460, 2),
                isbn,
                Date.valueOf("2021-12-21"),
                Set.of(newAuthor),
                Set.of(genre),
                new ArrayList<>()
        );
        var createdBook = bookStorage.create(newBook);
        assertThat(createdBook).isNotNull();
        em.detach(newAuthor);
        em.detach(genre);
        em.detach(createdBook);

        List<Book> actual = em.getEntityManager()
                .createQuery("select b from Book b where b.isbn = :isbn", Book.class)
                .setParameter("isbn", isbn)
                .getResultList();
        assertThat(actual.size()).isEqualTo(1);
        var actualBook = actual.get(0);

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(createdBook);
        assertAll("Inserted book",
                () -> assertEquals("Core Java, Volume I: Fundamentals, Twelfth Edition",
                        actualBook.getTitle()),
                () -> assertEquals(944, actualBook.getTotalPages()),
                () -> assertEquals(BigDecimal.valueOf(460, 2),
                        actualBook.getRating()),
                () -> assertEquals(isbn, actualBook.getIsbn()),
                () -> assertEquals(Date.valueOf("2021-12-21"), actualBook.getPublishedDate()),
                () -> assertThat(actualBook.getAuthors().size()).isEqualTo(1),
                () -> assertThat(new ArrayList<>(actualBook.getAuthors()).get(0).getLastName())
                        .isEqualTo("Horstmann"),
                () -> assertThat(actualBook.getGenres().size()).isEqualTo(1),
                () -> assertThat(new ArrayList<>(actualBook.getGenres()).get(0).getName())
                        .isEqualTo("Programming Languages")
        );
    }


}
