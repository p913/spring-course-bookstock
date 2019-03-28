package com.pvil.otuscourse.bookstockjdbc.shell;

import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
import com.pvil.otuscourse.bookstockjdbc.domain.Genre;
import com.pvil.otuscourse.bookstockjdbc.service.AuthorsService;
import com.pvil.otuscourse.bookstockjdbc.service.BookStockService;
import com.pvil.otuscourse.bookstockjdbc.service.GenresService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;

import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
public class ShellCommands {
    private static final String BOOK_SEARCH_CRITERION_NAME = "name";
    private static final String BOOK_SEARCH_CRITERION_ISBN = "isbn";
    private static final String BOOK_SEARCH_CRITERION_YEAR = "year";
    private static final String BOOK_SEARCH_CRITERION_AUTHOR = "author";
    private static final String BOOK_SEARCH_CRITERION_GENRE = "genre";

    private static final String SHOW_BOOKS = "books";
    private static final String SHOW_GENRES = "genres";
    private static final String SHOW_AUTHORS = "authors";

    private final Table EMPTY_TABLE = new TableBuilder(new ArrayTableModel(new Object[0][0])).build();

    private final BookStockService bookStockService;
    private final AuthorsService authorsService;
    private final GenresService genresService;

    public ShellCommands(BookStockService bookStockService, AuthorsService authorsService, GenresService genresService) {
        this.bookStockService = bookStockService;
        this.authorsService = authorsService;
        this.genresService = genresService;
    }

    @ShellMethod("Show all books, authors or genres")
    public Table show(@ShellOption(
            help = "Show " + SHOW_BOOKS + " (default), " + SHOW_AUTHORS + " or " + SHOW_GENRES,
            defaultValue = SHOW_BOOKS) String what) {
        if (SHOW_BOOKS.equals(what))
            return booksListToTable(bookStockService.getAllBooks());
        else if (SHOW_AUTHORS.equals(what))
            return authorsListToTable(authorsService.getAllAuthors());
        else if (SHOW_GENRES.equals(what))
            return genresListToTable(genresService.getAllGenres());

        return EMPTY_TABLE;
    }

    @ShellMethod(value = "Search books with defined criterion", key = {"search"})
    public Table searchBooks(@ShellOption(
            help="Criterion for search: " + BOOK_SEARCH_CRITERION_NAME + ", " + BOOK_SEARCH_CRITERION_ISBN + ", " +
                    BOOK_SEARCH_CRITERION_YEAR + ", " + BOOK_SEARCH_CRITERION_AUTHOR + " or " + BOOK_SEARCH_CRITERION_GENRE,
            defaultValue = BOOK_SEARCH_CRITERION_NAME)
            String criterion,
                             @ShellOption() String filter) {
        List<Book> books;
        if (BOOK_SEARCH_CRITERION_NAME.equals(criterion))
            books = bookStockService.getBookByName(filter);
        else if (BOOK_SEARCH_CRITERION_ISBN.equals(criterion))
            books = bookStockService.getBookByIsbn(filter);
        else if (BOOK_SEARCH_CRITERION_YEAR.equals(criterion))
            try {
                books = bookStockService.getBookByYear(Integer.parseInt(filter));
            } catch (NumberFormatException e) {
                return EMPTY_TABLE;
            }
        else if (BOOK_SEARCH_CRITERION_AUTHOR.equals(criterion))
            books = bookStockService.getBookByAuthor(filter);
        else if (BOOK_SEARCH_CRITERION_GENRE.equals(criterion))
            books = bookStockService.getBookByGenre(filter);
        else
            return EMPTY_TABLE;

        return booksListToTable(books);
    }

    @ShellMethod(key="addbook", value="Add book to stock")
    public String addBook(@ShellOption("--title") String name,
                          @ShellOption(defaultValue = "", help = "If 'author' not specified 'authorId' required") String author,
                          @ShellOption(defaultValue = "", help = "If 'genre' not specified 'genreId' required") String genre,
                          @ShellOption String isbn,
                          @ShellOption String publisher,
                          @ShellOption int year,
                          @ShellOption(defaultValue = "0", help = "If 'authorId' = 0 then 'author' required") int authorId,
                          @ShellOption(defaultValue = "0", help = "If 'genreId' = 0 then 'genre' required") int genreId) {
        if (authorId == 0 && author.equals(""))
            throw new IllegalArgumentException("Author or its Id must be specified");
        if (genreId == 0 && genre.equals(""))
            throw new IllegalArgumentException("Genre or its Id must be specified");

        return "Book added with new Id = " +
                bookStockService.addBook(new Book(name, isbn, publisher, year,
                        new Author(authorId, author), new Genre(genreId, genre)));
    }

    @ShellMethod(key="delbook", value="Delete book from stock by its Id")
    public String deleteBook(int id) {
        Book book = new Book();
        book.setId(id);
        if (bookStockService.deleteBook(book))
            return "Book deleted";
        else
            return String.format("Book with Id=%d not found", id);
    }

    @ShellMethod(key="editbook", value="Modify book in stock by its Id")
    public String updateBook(int id,
                          @ShellOption("--title") String name,
                          @ShellOption(defaultValue = "", help = "If 'author' not specified 'author-id' required") String author,
                          @ShellOption(defaultValue = "", help = "If 'genre' not specified 'genre-id' required") String genre,
                          @ShellOption String isbn,
                          @ShellOption String publisher,
                          @ShellOption int year,
                          @ShellOption(defaultValue = "0", help = "If 'author-id' = 0 then 'author' required") int authorId,
                          @ShellOption(defaultValue = "0", help = "If 'genre-id' = 0 then 'genre' required") int genreId) {
        if (authorId == 0 && author.equals(""))
            throw new IllegalArgumentException("Author or its Id must be specified");
        if (genreId == 0 && genre.equals(""))
            throw new IllegalArgumentException("Genre or its Id must be specified");

        bookStockService.updateBook(new Book(id, name, isbn, publisher, year, new Author(authorId, author), new Genre(genreId, genre)));

        return "Book modified";
    }

    @ShellMethod(key="addgenre", value="Add new genre")
    public String addGenre(String name) {
        return "Genre added with new Id = " + genresService.addGenre(new Genre(name));
    }

    @ShellMethod(key="editgenre", value="Modify genre by its Id")
    public String updateGenre(int id, String name) {
        genresService.updateGenre(new Genre(id, name));
        return "Genre modified";
    }

    @ShellMethod(key="delgenre", value="Delete genre by its Id")
    public String deleteGenre(int id) {
        Genre genre = new Genre();
        genre.setId(id);
        if (genresService.deleteGenre(genre))
            return "Genre deleted";
        else
            return String.format("Genre with Id=%d not found", id);
    }

    @ShellMethod(key="addauthor", value="Add new author")
    public String addAuthor(String name) {
        return "Author added with new Id = " + authorsService.addAuthor(new Author(name));
    }

    @ShellMethod(key="editauthor", value="Modify author by its Id")
    public String updateAuthor(int id, String name) {
        authorsService.updateAuthor(new Author(id, name));
        return "Author modified";
    }

    @ShellMethod(key="delauthor", value="Delete author by its Id")
    public String deleteAuthor(int id) {
        Author author = new Author();
        author.setId(id);
        if (authorsService.deleteAuthor(author))
            return "Author deleted";
        else
            return String.format("Author with Id=%d not found", id);
    }

    private Table booksListToTable(List<Book> books) {
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        header.put("name", "Title");
        header.put("author.name", "Author");
        header.put("genre.name", "Genre");
        header.put("isbn", "ISBN");
        header.put("publisher", "Publisher");
        header.put("year", "Year");
        header.put("id", "Id");

        return new TableBuilder(new BeanListTableModel<Book>(books, header)).addFullBorder(BorderStyle.oldschool).build();
    }

    private Table authorsListToTable(List<Author> authors) {
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        header.put("name", "Name");
        header.put("id", "Id");

        return new TableBuilder(new BeanListTableModel<Author>(authors, header)).addFullBorder(BorderStyle.oldschool).build();
    }

    private Table genresListToTable(List<Genre> genres) {
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        header.put("name", "Name");
        header.put("id", "Id");

        return new TableBuilder(new BeanListTableModel<Genre>(genres, header)).addFullBorder(BorderStyle.oldschool).build();
    }

}
