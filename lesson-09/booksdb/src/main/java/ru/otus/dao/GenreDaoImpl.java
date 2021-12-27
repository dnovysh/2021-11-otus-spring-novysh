package ru.otus.dao;

import lombok.val;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcOperations jdbc;

    public GenreDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public int count() {
        return Optional.ofNullable(
                jdbc.queryForObject("select count(*) from genre", Integer.class)
        ).orElse(0);
    }

    @Override
    public Genre getByIdWithoutPopulateChildrenList(String id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject(
                    "select id, name, parent_id from genre where id = :id",
                    params, new GenreMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAllWithoutPopulateChildrenList() {
        return jdbc.query("select id, name, parent_id from genre", new GenreMapper());
    }

    @Override
    public Genre getEntireHierarchyStartWithId(@NonNull String id) {
        Map<String, Object> params = Collections.singletonMap("id", id + "%");
        SqlRowSet rs = namedParameterJdbcOperations.queryForRowSet(
                "select id, name, parent_id from genre where id like :id order by parent_id desc nulls last , id",
                params
        );

        List<Genre> genres = mapHierarchyFromSqlRowSet(rs, id);

        if (genres.size() != 1 || !genres.get(0).id().equals(id)) {
            return null;
        }

        return genres.get(0);
    }

    @Override
    public List<Genre> getEntireHierarchyStartWithRoot() {
        SqlRowSet rs = jdbc.queryForRowSet(
                "select id, name, parent_id from genre order by parent_id desc nulls last , id"
        );

        return mapHierarchyFromSqlRowSet(rs, null);
    }

    private List<Genre> mapHierarchyFromSqlRowSet(SqlRowSet rs, String rootId) {
        Map<String, List<Genre>> childrenById = new HashMap<>();
        List<Genre> root = new ArrayList<>();

        while (rs.next()) {
            val id = rs.getString("id");
            val name = rs.getString("name");
            val parentId = rs.getString("parent_id");
            List<Genre> children = childrenById.get(id);
            var genre = new Genre(id, name, parentId, children);
            assert id != null;
            if (id.equals(rootId) || parentId == null) {
                root.add(genre);
                continue;
            }
            List<Genre> siblings = childrenById.computeIfAbsent(parentId, k -> new ArrayList<>());
            siblings.add(genre);
        }

        return root;
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getString("id");
            var name = rs.getString("name");
            var parentId = rs.getString("parent_id");
            return new Genre(id, name, parentId, null);
        }
    }
}
