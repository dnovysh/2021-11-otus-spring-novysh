package ru.otus.uow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.core.dto.BookReviewsViewDto;
import ru.otus.core.dto.BookUpdateDto;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private static final List<Review> EXISTING_BOOK_REVIEWS =
            List.of(
                    new Review(103, 529, "THE Kotlin book", null,
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2018-08-01"), false),
                    new Review(265, 529,
                            "Great introduction to Kotlin",
                            "We started using Kotlin in 2019 and I used this book to introduce the team to it...",
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2020-12-28"), false),
                    new Review(230, 529,
                            "Awesome reference",
                            "I really love this book, and expect to be referring back to it for many years to come.",
                            BigDecimal.valueOf(500, 2),
                            Date.valueOf("2021-03-25"), false)
            );
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
                    EXISTING_BOOK_REVIEWS
            );
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

    private static final int NON_EXISTENT_BOOK_ID = 100;

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

    @DisplayName("return the expected book by its id")
    @Test
    void shouldReturnExpectedBookById() {
        var actualBook = bookStorage.findById(EXISTING_BOOK.getId());
        assertThat(actualBook)
                .isNotEmpty().get()
                .usingRecursiveComparison()
                .ignoringCollectionOrder().isEqualTo(EXISTING_BOOK);
    }

    @DisplayName("return optional empty for non-existent book id")
    @Test
    void shouldReturnEmptyForNonExistentBookId() {
        var actualBook = bookStorage.findById(NON_EXISTENT_BOOK_ID);
        assertThat(actualBook).isEmpty();
    }

    @DisplayName("return a list of all books")
    @Test
    void shouldReturnAllBooksList() {
        var actualBooks = bookStorage.findAll();
        assertThat(actualBooks)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(EXPECTED_ALL_BOOK_LIST);
    }

    @DisplayName("return short book info with reviews by book id")
    @Test
    void shouldReturnBookReviewsByBookId() {
        var expectedBookReviewsViewDto = new BookReviewsViewDto(
                EXISTING_BOOK.getId(),
                EXISTING_BOOK.getTitle(),
                EXISTING_BOOK.getPublishedDate(),
                EXISTING_BOOK.getReviews()
        );
        var actualBookReviewsViewDto =
                bookStorage.findBookReviewsById(EXISTING_BOOK.getId());
        assertThat(actualBookReviewsViewDto)
                .isNotEmpty().get()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedBookReviewsViewDto);
    }

    @DisplayName("insert book correctly")
    @Test
    void shouldInsertBookCorrectly() {
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

    @DisplayName("update book correctly")
    @Test
    void shouldUpdateBookCorrectly() {
        int bookId = 253;
        var originalBook = em.find(Book.class, bookId);
        assertThat(originalBook).isNotNull();
        assertThat(originalBook.getAuthors().size()).isEqualTo(4);
        assertThat(originalBook.getGenres().size()).isEqualTo(2);
        assertThat(originalBook.getReviews().size()).isZero();
        em.detach(originalBook);
        var expectedTitle = "Foo Bar";
        var expectedTotalPages = 900;
        var expectedRating = BigDecimal.valueOf(250, 2);
        var expectedIsbn = "9785970607923";
        var expectedPublishedDate = Date.valueOf("1986-03-25");
        var expectedBook = new Book(
                bookId, expectedTitle, expectedTotalPages, expectedRating, expectedIsbn,
                expectedPublishedDate,
                originalBook.getAuthors(),
                originalBook.getGenres(),
                originalBook.getReviews()
        );
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                bookId, expectedTitle, expectedTotalPages, expectedRating, expectedIsbn,
                expectedPublishedDate
        );
        var updatedBook = bookStorage.update(bookUpdateDto);
        em.flush();
        assertThat(updatedBook.getAuthors().size()).isEqualTo(4);
        assertThat(updatedBook.getGenres().size()).isEqualTo(2);
        assertThat(updatedBook.getReviews().size()).isZero();
        em.detach(updatedBook);
        var actualBook = em.find(Book.class, bookId);
        assertThat(actualBook.getAuthors().size()).isEqualTo(4);
        assertThat(actualBook.getGenres().size()).isEqualTo(2);
        assertThat(actualBook.getReviews().size()).isZero();
        assertThat(actualBook).isNotSameAs(updatedBook);
        assertThat(updatedBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("delete book by its ID correctly")
    @Test
    void shouldDeleteBookCorrectly() {
        final int existingBookId = EXISTING_BOOK.getId();
        var originalCountBookById = em.getEntityManager()
                .createQuery("select count(b) from Book b where b.id = :id", Long.class)
                .setParameter("id", existingBookId)
                .getSingleResult();
        assertThat(originalCountBookById).isEqualTo(1);
        var originalCountBook = em.getEntityManager()
                .createQuery("select count(b) from Book b", Long.class)
                .getSingleResult();
        bookStorage.deleteById(existingBookId);
        var actualCountBookById = em.getEntityManager()
                .createQuery("select count(b) from Book b where b.id = :id", Long.class)
                .setParameter("id", existingBookId)
                .getSingleResult();
        assertThat(actualCountBookById).isZero();
        var actualCountBook = em.getEntityManager()
                .createQuery("select count(b) from Book b", Long.class)
                .getSingleResult();
        assertThat(actualCountBook).isEqualTo(originalCountBook - 1);
    }

    @DisplayName("add author to book by their IDs")
    @Test
    void shouldAddAuthorToBookById() {
        final int bookId = 757;
        final int authorId = 987;
        var originalBook = em.find(Book.class, bookId);
        var author = em.find(Author.class, authorId);
        assertThat(originalBook).isNotNull();
        assertThat(originalBook.getAuthors()).doesNotContain(author);
        em.detach(originalBook);
        var optionalBookWithAddedAuthor =
                bookStorage.addAuthorToBookById(bookId, authorId);
        em.flush();
        assertThat(optionalBookWithAddedAuthor).isNotEmpty();
        var bookWithAddedAuthor = optionalBookWithAddedAuthor.get();
        assertThat(bookWithAddedAuthor.getId()).isEqualTo(bookId);
        assertThat(bookWithAddedAuthor.getAuthors()).containsOnlyOnce(author);
        em.detach(bookWithAddedAuthor);
        var actualBook = em.find(Book.class, bookId);
        assertThat(actualBook).isNotSameAs(bookWithAddedAuthor);
        assertThat(actualBook.getAuthors()).containsOnlyOnce(author);
    }

    @DisplayName("add author should return raise DataIntegrityViolationException if book does not exist")
    @Test
    void addAuthorShouldRaiseExceptionIfBookDoesNotExist() {
        final int nonExistentBookId = 200;
        final int existingAuthorId = 987;
        assertThat(bookStorage.findById(nonExistentBookId)).isEmpty();
        assertThat(em.find(Author.class, existingAuthorId)).isNotNull();
        assertThatThrownBy(
                () -> {
                    bookStorage.addAuthorToBookById(nonExistentBookId, existingAuthorId);
                    em.flush();
                }
        ).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("add author should return raise DataIntegrityViolationException if author does not exist")
    @Test
    void addAuthorShouldRaiseExceptionIfAuthorDoesNotExist() {
        final int existingBookId = 757;
        final int nonExistentAuthorId = 200;
        assertThat(bookStorage.findById(existingBookId)).isNotEmpty();
        assertThat(em.find(Author.class, nonExistentAuthorId)).isNull();
        assertThatThrownBy(
                () -> {
                    bookStorage.addAuthorToBookById(existingBookId, nonExistentAuthorId);
                    em.flush();
                }
        ).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }


}
