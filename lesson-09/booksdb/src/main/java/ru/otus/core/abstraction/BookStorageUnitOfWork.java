package ru.otus.core.abstraction;

import ru.otus.core.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookStorageUnitOfWork {

    public long count();

    Optional<Book> findById(Integer id);

    public List<Book> findAll();

    Book create(Book book);

    Book update(Book book);

    public void deleteById(Integer id);

    Optional<Book> addAuthorToBookById(int bookId, int authorId);

    Optional<Book> removeAuthorFromBookById(int bookId, int authorId);

    Optional<Book> addGenreToBookById(int bookId, String genreId);

    Optional<Book> removeGenreFromBookById(int bookId, String genreId);
}
