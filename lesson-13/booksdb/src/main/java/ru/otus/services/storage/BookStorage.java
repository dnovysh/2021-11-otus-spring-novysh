package ru.otus.services.storage;

import ru.otus.dto.BookUpdateDto;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookStorage {
    long count();

    Optional<Book> findById(String id);

    List<Book> findAll();

    Book create(Book book);

    Book update(BookUpdateDto bookUpdateDto);

    void deleteById(String id);

    void restoreById(String id);

    void addAuthor(String bookId, Author author);

    void removeAuthor(String bookId, Author author);

    void addGenre(String bookId, Genre genre);

    void removeGenre(String bookId, Genre genre);
}
