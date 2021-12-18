package ru.otus.dao.helper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Author;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookAuthorDaoImpl implements BookAuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookAuthorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> getAllByBookIdWithoutChildren(int bookId) {
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
}
