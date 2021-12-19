package ru.otus.dao.helper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Author;

import java.util.*;

@Repository
public class BookAuthorDaoImpl implements BookAuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookAuthorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> getAllByBookId(int bookId) {
        Map<String, Object> params = Collections.singletonMap("book_id", bookId);
        return namedParameterJdbcOperations.query(
                "select id, first_name, middle_name, last_name " +
                        "from book_author ba join author a on ba.author_id = a.id " +
                        "where ba.book_id = :book_id",
                params,
                (rs, rowNum) -> new Author(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name")
                )
        );
    }

    @Override
    public Map<Integer, List<Author>> getAll() {
        return namedParameterJdbcOperations.query(
                "select ba.book_id as book_id, id, first_name, middle_name, last_name " +
                        "from book_author ba join author a on ba.author_id = a.id ",
                (rs) -> {
                    var result = new HashMap<Integer, List<Author>>();
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        var author = new Author(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("middle_name"),
                                rs.getString("last_name"));
                        List<Author> authors = result.computeIfAbsent(bookId, k -> new ArrayList<>());
                        authors.add(author);
                    }
                    return result;
                }
        );
    }

    @Override
    public void insert(BookAuthor bookAuthor) {
        namedParameterJdbcOperations.update(
                "insert into book_author (book_id, author_id) values (:book_id, :author_id)",
                Map.of("book_id", bookAuthor.bookId(), "author_id", bookAuthor.authorId()));
    }

    @Override
    public boolean delete(BookAuthor bookAuthor) {
        int status = namedParameterJdbcOperations.update(
                "delete from book_author where book_id = :book_id and author_id = :author_id",
                Map.of("book_id", bookAuthor.bookId(), "author_id", bookAuthor.authorId())
        );
        return status > 0;
    }

    @Override
    public boolean exists(BookAuthor bookAuthor) {
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select count(*) from book_author where book_id = :book_id and author_id = :author_id",
                Map.of("book_id", bookAuthor.bookId(), "author_id", bookAuthor.authorId()),
                Integer.class)
        ).orElse(0) == 1;
    }
}
