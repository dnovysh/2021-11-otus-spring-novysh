package ru.otus.core.abstraction;

import ru.otus.core.dto.BookReviewsViewDto;
import ru.otus.core.dto.BookUpdateDto;
import ru.otus.core.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookStorageService {

    long count();

    Optional<Book> findById(Integer id);

    List<Book> findAll();

    Optional<BookReviewsViewDto> findBookReviewsById(Integer id);

    Book create(Book book);

    Book update(BookUpdateDto bookUpdateDto);

    void deleteById(Integer id);

    Optional<Book> addAuthorToBookById(int bookId, int authorId);

    Optional<Book> removeAuthorFromBookById(int bookId, int authorId);

    Optional<Book> addGenreToBookById(int bookId, String genreId);

    Optional<Book> removeGenreFromBookById(int bookId, String genreId);
}
