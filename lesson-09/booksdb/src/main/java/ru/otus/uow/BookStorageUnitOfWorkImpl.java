package ru.otus.uow;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.core.abstraction.AuthorStorageUnitOfWork;
import ru.otus.core.abstraction.BookStorageUnitOfWork;
import ru.otus.core.abstraction.GenreStorageUnitOfWork;
import ru.otus.core.dto.BookReviewsViewDto;
import ru.otus.core.dto.BookUpdateDto;
import ru.otus.core.entity.Author;
import ru.otus.core.entity.Book;
import ru.otus.core.entity.Genre;
import ru.otus.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookStorageUnitOfWorkImpl implements BookStorageUnitOfWork {

    private final BookRepository bookRepository;
    private final AuthorStorageUnitOfWork authorStorage;
    private final GenreStorageUnitOfWork genreStorage;

    public BookStorageUnitOfWorkImpl(BookRepository bookRepository,
                                     AuthorStorageUnitOfWork authorStorage,
                                     GenreStorageUnitOfWork genreStorage) {
        this.bookRepository = bookRepository;
        this.authorStorage = authorStorage;
        this.genreStorage = genreStorage;
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return bookRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookReviewsViewDto> findBookReviewsById(Integer id) {
        return bookRepository.findById(id).map((b) -> {
            return new BookReviewsViewDto(
                    b.getId(),
                    b.getTitle(),
                    b.getPublishedDate(),
                    new ArrayList<>(b.getReviews()));
        });
    }

    @Transactional
    @Override
    public Book create(Book book) {
        if (book == null) {
            throw new IllegalArgumentException(
                    "The book being created must not be null");
        }

        if (book.getId() != null) {
            throw new IllegalArgumentException(
                    "The identifier for the book being created must not be set");
        }

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book update(BookUpdateDto bookUpdateDto) {
        if (bookUpdateDto == null) {
            throw new IllegalArgumentException(
                    "The book being updated must not be null");
        }
        if (bookUpdateDto.id() == null) {
            throw new IllegalArgumentException(
                    "The identifier of the book being updated must not be null");
        }
        var optionalBook = bookRepository.findById(bookUpdateDto.id());
        if (optionalBook.isEmpty()) {
            return null;
        }
        return optionalBook.map(bookUpdateDto::applyToBook).get();
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<Book> addAuthorToBookById(int bookId, int authorId) {
        Optional<BookAuthor> optionalBookAuthor = findBookAndAuthor(bookId, authorId);
        if (optionalBookAuthor.isEmpty()) {
            throw new DataIntegrityViolationException("Book or author does not exist");
        }
        Book book = optionalBookAuthor.get().book;
        Author author = optionalBookAuthor.get().author;
        book.getAuthors().add(author);
        return Optional.of(book);
    }

    @Transactional
    @Override
    public Optional<Book> removeAuthorFromBookById(int bookId, int authorId) {
        Optional<BookAuthor> optionalBookAuthor = findBookAndAuthor(bookId, authorId);
        if (optionalBookAuthor.isEmpty()) {
            return Optional.empty();
        }
        Book book = optionalBookAuthor.get().book;
        if (book.getAuthors().remove(optionalBookAuthor.get().author)) {
            return Optional.of(book);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Book> addGenreToBookById(int bookId, String genreId) {
        Optional<BookGenre> optionalBookGenre = findBookAndGenre(bookId, genreId);
        if (optionalBookGenre.isEmpty()) {
            return Optional.empty();
        }
        Book book = optionalBookGenre.get().book;
        Genre genre = optionalBookGenre.get().genre;
        book.getGenres().add(genre);
        return Optional.of(book);
    }

    @Transactional
    @Override
    public Optional<Book> removeGenreFromBookById(int bookId, String genreId) {
        Optional<BookGenre> optionalBookGenre = findBookAndGenre(bookId, genreId);
        if (optionalBookGenre.isEmpty()) {
            return Optional.empty();
        }
        Book book = optionalBookGenre.get().book;
        if (book.getGenres().remove(optionalBookGenre.get().genre)) {
            return Optional.of(book);
        }
        return Optional.empty();
    }

    private Optional<BookAuthor> findBookAndAuthor(int bookId, int authorId) {
        Optional<Book> optionalBook = findById(bookId);
        Optional<Author> optionalAuthor = authorStorage.findById(authorId);
        if (optionalBook.isEmpty() || optionalAuthor.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new BookAuthor(optionalBook.get(), optionalAuthor.get()));
    }

    private Optional<BookGenre> findBookAndGenre(int bookId, String genreId) {
        Optional<Book> optionalBook = findById(bookId);
        Optional<Genre> optionalGenre = genreStorage.findById(genreId);
        if (optionalBook.isEmpty() || optionalGenre.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new BookGenre(optionalBook.get(), optionalGenre.get()));
    }

    private record BookAuthor(Book book, Author author) {
    }

    private record BookGenre(Book book, Genre genre) {
    }
}
