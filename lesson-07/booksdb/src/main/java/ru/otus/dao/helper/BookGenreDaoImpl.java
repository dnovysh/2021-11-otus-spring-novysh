package ru.otus.dao.helper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Genre;

import java.util.*;

@Repository
public class BookGenreDaoImpl implements BookGenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookGenreDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> getAllByBookIdWithoutPopulateChildrenList(int bookId) {
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

    @Override
    public Map<Integer, List<Genre>> getAllWithoutPopulateChildrenList() {
        return namedParameterJdbcOperations.query(
                "select bg.book_id as book_id, id, name, parent_id " +
                        "from book_genre as bg join genre as g on bg.genre_id = g.id ",
                (rs) -> {
                    var result = new HashMap<Integer, List<Genre>>();
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        var genre = new Genre(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("parent_id"),
                                null);
                        List<Genre> genres = result.computeIfAbsent(bookId, k -> new ArrayList<>());
                        genres.add(genre);
                    }
                    return result;
                }
        );
    }

    @Override
    public void insert(BookGenre bookGenre) {
        namedParameterJdbcOperations.update(
                "insert into book_genre (book_id, genre_id) values (:book_id, :genre_id)",
                Map.of("book_id", bookGenre.bookId(), "genre_id", bookGenre.genreId()));
    }

    @Override
    public boolean delete(BookGenre bookGenre) {
        int status = namedParameterJdbcOperations.update(
                "delete from book_genre where book_id = :book_id and genre_id = :genre_id",
                Map.of("book_id", bookGenre.bookId(), "genre_id", bookGenre.genreId())
        );
        return status > 0;
    }

    @Override
    public boolean exists(BookGenre bookGenre) {
        return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select count(*) from book_genre where book_id = :book_id and genre_id = :genre_id",
                Map.of("book_id", bookGenre.bookId(), "genre_id", bookGenre.genreId()),
                Integer.class)
        ).orElse(0) == 1;
    }
}
