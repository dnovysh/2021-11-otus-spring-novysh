package ru.otus.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.dao.helper.*;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;

import java.util.*;

@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcOperations jdbc;
    private final BookAuthorDao bookAuthorDao;
    private final BookGenreDao bookGenreDao;

    public BookDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations,
                       BookAuthorDao bookAuthorDao,
                       BookGenreDao bookGenreDao) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.bookAuthorDao = bookAuthorDao;
        this.bookGenreDao = bookGenreDao;
    }

    @Override
    public int count() {
        return Optional.ofNullable(
                jdbc.queryForObject("select count(*) from book", Integer.class)
        ).orElse(0);
    }

    @Override
    public int insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource(Map.of(
                "title", book.title(),
                "total_pages", book.totalPages(),
                "rating", book.rating(),
                "isbn", book.isbn(),
                "published_date", book.publishedDate()));
        namedParameterJdbcOperations.update(
                "insert into book (title, total_pages, rating, isbn, published_date)" +
                        " values (:title, :total_pages, :rating, :isbn, :published_date)",
                params, keyHolder, new String[]{"id"}
        );
        assert keyHolder.getKey() != null;
        return keyHolder.getKey().intValue();
    }

    @Override
    public Book getById(int id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select id, title, total_pages, rating, isbn, published_date from book where id = :id",
                params,
                (rs, rowNum) -> {
                    int bookId = rs.getInt("id");
                    return new Book(
                            bookId,
                            rs.getString("title"),
                            rs.getInt("total_pages"),
                            rs.getBigDecimal("rating"),
                            rs.getString("isbn"),
                            rs.getDate("published_date"),
                            bookAuthorDao.getAllByBookId(bookId),
                            bookGenreDao.getAllByBookIdWithoutPopulateChildrenList(bookId));
                }
        );
    }

    @Override
    public List<Book> getAll() {
        Map<Integer, List<Author>> allActualAuthors = bookAuthorDao.getAll();
        Map<Integer, List<Genre>> allActualGenres = bookGenreDao.getAllWithoutPopulateChildrenList();
        List<PlainBook> plainBooks = namedParameterJdbcOperations.query(
                "select id, title, total_pages, rating, isbn, published_date from book",
                (rs, rowNum) -> {
                    return new PlainBook(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("total_pages"),
                            rs.getBigDecimal("rating"),
                            rs.getString("isbn"),
                            rs.getDate("published_date"));
                }
        );
        var result = new ArrayList<Book>();
        for (PlainBook plainBook : plainBooks) {
            result.add(new Book(
                    plainBook.id(), plainBook.title(), plainBook.totalPages(),
                    plainBook.rating(), plainBook.isbn(), plainBook.publishedDate(),
                    Optional.ofNullable(allActualAuthors.get(plainBook.id())).orElse(new ArrayList<>()),
                    Optional.ofNullable(allActualGenres.get(plainBook.id())).orElse(new ArrayList<>())
            ));
        }
        return result;
    }

    @Override
    public boolean update(Book book) {
        int status = namedParameterJdbcOperations.update(
                "update book set title = :title, total_pages = :total_pages," +
                        " rating = :rating, isbn = :isbn, published_date = :published_date" +
                        " where id = :id",
                new MapSqlParameterSource(Map.of(
                        "id", book.id(),
                        "title", book.title(),
                        "total_pages", book.totalPages(),
                        "rating", book.rating(),
                        "isbn", book.isbn(),
                        "published_date", book.publishedDate()))
        );
        return status > 0;
    }

    @Override
    public boolean deleteById(int id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        int status = namedParameterJdbcOperations.update(
                "delete from book where id = :id", params
        );
        return status > 0;
    }

    @Override
    public void attachAuthorToBookById(int bookId, int authorId) {
        bookAuthorDao.insert(new BookAuthor(bookId, authorId));
    }

    @Override
    public boolean detachAuthorFromBookById(int bookId, int authorId) {
        return bookAuthorDao.delete(new BookAuthor(bookId, authorId));
    }

    @Override
    public void attachGenreToBookById(int bookId, String genreId) {
        bookGenreDao.insert(new BookGenre(bookId, genreId));
    }

    @Override
    public boolean detachGenreFromBookById(int bookId, String genreId) {
        return bookGenreDao.delete(new BookGenre(bookId, genreId));
    }
}
