package ru.otus.dao;

import ru.otus.dao.dto.BookInsertDto;
import ru.otus.dao.dto.BookUpdateDto;
import ru.otus.domain.Book;

import java.util.List;

public interface BookDao {

    int count();

    int insert(BookInsertDto bookInsertDto);

    Book getById(int id);

    List<Book> getAll();

    boolean update(BookUpdateDto bookUpdateDto);

    boolean deleteById(int id);

    boolean attachAuthorToBookById(int bookId, int authorId);

    boolean detachAuthorFromBookById(int bookId, int authorId);

    void attachGenreToBookById(int bookId, String genreId);

    boolean detachGenreFromBookById(int bookId, String genreId);
}
