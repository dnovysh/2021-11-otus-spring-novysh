package ru.otus.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.otus.dao.helper.BookAuthor;
import ru.otus.dao.helper.BookAuthorDao;
import ru.otus.dao.helper.BookGenreDao;
import ru.otus.domain.Author;
import ru.otus.domain.Book;

import java.util.*;
import java.util.stream.Collectors;

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
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                "title", book.title(),
                "total_pages", book.totalPages(),
                "rating", book.rating(),
                "isbn", book.isbn(),
                "published_date", book.publishedDate()));
        namedParameterJdbcOperations.update(
                "insert into book (title, total_pages, rating, isbn, published_date)" +
                        " values (:title, :total_pages, :rating, :isbn, :published_date)",
                parameters, keyHolder, new String[]{"id"}
        );
        assert keyHolder.getKey() != null;
        final int bookId = keyHolder.getKey().intValue();

        var authorsBatch = new ArrayList<BookAuthor>();
        for (Author author : book.authors()) {
            authorsBatch.add(new BookAuthor(bookId, author.id()));
        }



        namedParameterJdbcOperations.update(

        return bookId;
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
                            bookAuthorDao.getAllByBookIdWithoutChildren(bookId),
                            bookGenreDao.getAllByBookId(bookId));
                }
        );
    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public void updateById(int id) {

    }

    @Override
    public void deleteById(int id) {

    }
}
