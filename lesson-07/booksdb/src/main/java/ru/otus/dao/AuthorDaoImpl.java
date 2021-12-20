package ru.otus.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcOperations jdbc;

    public AuthorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public int count() {
        return Optional.ofNullable(
                jdbc.queryForObject("select count(*) from author", Integer.class)
        ).orElse(0);
    }

    @Override
    public Author getById(int id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject(
                    "select id, first_name, middle_name, last_name from author where id = :id",
                    params, new AuthorMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, first_name, middle_name, last_name from author", new AuthorMapper()
        );
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getInt("id");
            var firstName = rs.getString("first_name");
            var middleName = rs.getString("middle_name");
            var lastName = rs.getString("last_name");
            return new Author(id, firstName, middleName, lastName);
        }
    }
}
