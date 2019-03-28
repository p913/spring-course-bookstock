package com.pvil.otuscourse.bookstockjdbc.dao;

import com.pvil.otuscourse.bookstockjdbc.domain.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GenreDaoDb implements GenreDao {
    private final RowMapper<Genre> mapperGenre = (rs, i) ->
            new Genre(rs.getInt("GenreId"), rs.getString("GenreName"));

    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoDb(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public List<Genre> getAll() {
        String sql = "select GenreId, GenreName from Genres order by GenreName";
        return jdbc.query(sql, mapperGenre);
    }

    @Override
    public Optional<Genre> getById(int id) {
        String sql = "select GenreId, GenreName from Genres where GenreId = :genreId";
        Map<String, Object> params = Collections.singletonMap("genreId", id);
        try {
            return Optional.of(jdbc.queryForObject(sql, params, mapperGenre));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getByName(String filter) {
        String sql = "select GenreId, GenreName from Genres where GenreName like :filter order by GenreName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + filter + "%");
        return jdbc.query(sql, params, mapperGenre);
    }


    @Override
    public int add(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Genres (GenreName) values (:genreName)";
        jdbc.update(sql, new MapSqlParameterSource().addValue("genreName", genre.getName()), keyHolder);
        return Integer.parseInt(keyHolder.getKeys().get("genreid").toString());
    }

    @Override
    public boolean delete(Genre genre) {
        String sql = "delete from Genres where GenreId = :genreId";
        Map<String, Object> params = Collections.singletonMap("genreId", genre.getId());
        return jdbc.update(sql, params) > 0;
    }

    @Override
    public boolean update(Genre genre) {
        String sql = "update Genres set GenreName = :genreName where GenreId = :genreId";
        Map<String, Object> params = new HashMap<>();
        params.put("genreId", genre.getId());
        params.put("genreName", genre.getName());
        return jdbc.update(sql, params) > 0;
    }}
