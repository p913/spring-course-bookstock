package com.pvil.otuscourse.bookstockjdbc.dao;

import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AuthorDaoDb implements AuthorDao {
    private final RowMapper<Author> mapperAuthor = (rs, i) ->
            new Author(rs.getInt("AuthorId"), rs.getString("AuthorName"));

    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoDb(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Author> getAll() {
        String sql = "select AuthorId, AuthorName from Authors order by AuthorName";
        return jdbc.query(sql, mapperAuthor);
    }

    @Override
    public Optional<Author> getById(int id) {
        String sql = "select AuthorId, AuthorName from Authors where AuthorId = :authorId";
        Map<String, Object> params = Collections.singletonMap("authorId", id);
        try {
            return Optional.of(jdbc.queryForObject(sql, params, mapperAuthor));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> getByName(String filter) {
        String sql = "select AuthorId, AuthorName from Authors where AuthorName like :filter order by AuthorName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + filter + "%");
        return jdbc.query(sql, params, mapperAuthor);
    }

    @Override
    public int add(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Authors (AuthorName) values (:authorName)";
        jdbc.update(sql, new MapSqlParameterSource().addValue("authorName", author.getName()), keyHolder);
        return Integer.parseInt(keyHolder.getKeys().get("authorid").toString());
    }

    @Override
    public boolean delete(Author author) {
        String sql = "delete from Authors where AuthorId = :authorId";
        Map<String, Object> params = Collections.singletonMap("authorId", author.getId());
        return jdbc.update(sql, params) > 0;
    }

    @Override
    public boolean update(Author author) {
        String sql = "update Authors set AuthorName = :authorName where AuthorId = :authorId";
        Map<String, Object> params = new HashMap<>();
        params.put("authorId", author.getId());
        params.put("authorName", author.getName());
        return jdbc.update(sql, params) > 0;
    }
}
