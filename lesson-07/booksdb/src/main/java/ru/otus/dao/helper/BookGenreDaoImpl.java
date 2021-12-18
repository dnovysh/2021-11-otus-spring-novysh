package ru.otus.dao.helper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookGenreDaoImpl implements BookGenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookGenreDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> getAllByBookIdWithoutChildren(int bookId) {
        Map<String, Object> params = Collections.singletonMap("book_id", bookId);
        return namedParameterJdbcOperations.query(
                "select id, name, parent_id " +
                        "from book_genre bg join genre g on bg.genre_id = g.id " +
                        "where bg.book_id = :book_id",
                params,
                (rs, rowNum) -> new Genre(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("parent_id"),
                        null
                )
        );
    }
}
