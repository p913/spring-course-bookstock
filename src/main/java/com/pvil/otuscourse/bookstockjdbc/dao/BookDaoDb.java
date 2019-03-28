package com.pvil.otuscourse.bookstockjdbc.dao;

import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
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
public class BookDaoDb implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    private RowMapper<Book> mapperBook = (rs, i) ->
        new Book(rs.getInt("BookId"), rs.getString("BookName"), rs.getString("ISBN"),
                rs.getString("Publisher"), rs.getInt("Year"),
                new Author(rs.getInt("AuthorId"), rs.getString("AuthorName")),
                new Genre(rs.getInt("GenreId"), rs.getString("GenreName")));

    public BookDaoDb(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Book> getAll() {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        return jdbc.query(sql, mapperBook);
    }

    @Override
    public List<Book> getByName(String nameFilter) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where b.BookName like :filter and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + nameFilter + "%");
        return jdbc.query(sql, params, mapperBook);
    }

    @Override
    public List<Book> getByGenre(String genreFilter) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where g.GenreName like :filter and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + genreFilter + "%");
        return jdbc.query(sql, params, mapperBook);
    }

    @Override
    public List<Book> getByAuthor(String authorFilter) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where a.AuthorName like :filter and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + authorFilter + "%");
        return jdbc.query(sql, params, mapperBook);
    }

    @Override
    public List<Book> getByIsbn(String isbnFilter) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where b.ISBN like :filter and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        Map<String, Object> params = Collections.singletonMap("filter", "%" + isbnFilter + "%");
        return jdbc.query(sql, params, mapperBook);
    }

    @Override
    public List<Book> getByYear(int year) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where b.Year = :filter and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId " +
                " order by BookName";
        Map<String, Object> params = Collections.singletonMap("filter", year);
        return jdbc.query(sql, params, mapperBook);
    }

    @Override
    public Optional<Book> getById(int id) {
        String sql = "select b.BookId, b.BookName, b.ISBN, b.Publisher, b.Year, b.AuthorId, b.GenreId, a.AuthorName, g.GenreName " +
                " from Books b, Authors a, Genres g " +
                " where b.BookId = :bookId and a.AuthorId = b.AuthorId and b.GenreId = g.GenreId ";
        Map<String, Object> params = Collections.singletonMap("bookId", id);
        try {
            return Optional.of(jdbc.queryForObject(sql, params, mapperBook));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int add(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId) " +
                " values (:bookName, :isbn, :publisher, :year, :authorId, :genreId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookName", book.getName())
            .addValue("isbn", book.getIsbn())
            .addValue("publisher", book.getPublisher())
            .addValue("year", book.getYear())
            .addValue("authorId", book.getAuthor().getId())
            .addValue("genreId", book.getGenre().getId());
        jdbc.update(sql, params, keyHolder);

        return Integer.parseInt(keyHolder.getKeys().get("bookid").toString());
    }

    @Override
    public boolean delete(Book book) {
        String sql = "delete from Books where BookId = :bookId ";
        Map<String, Object> params = Collections.singletonMap("bookId", book.getId());
        return jdbc.update(sql, params) > 0;
    }

    @Override
    public boolean update(Book book) {
        String sql = "update Books " +
                " set BookName = :bookName, " +
                    " ISBN = :isbn, " +
                    " Publisher = :publisher, " +
                    " Year = :year, " +
                    " AuthorId = :authorId, " +
                    " GenreId = :genreId " +
                " where BookId = :bookId ";
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("bookName", book.getName());
        params.put("isbn", book.getIsbn());
        params.put("publisher", book.getPublisher());
        params.put("year", book.getYear());
        params.put("authorId", book.getAuthor().getId());
        params.put("genreId", book.getGenre().getId());
        return jdbc.update(sql, params) > 0;
    }

}
