package com.pvil.otuscourse.bookstockjpajpql.shell;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;
import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;
import com.pvil.otuscourse.bookstockjpajpql.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

@ShellComponent
public class ShellCommands {
    private static final String BOOK_SEARCH_CRITERION_NAME = "name";
    private static final String BOOK_SEARCH_CRITERION_ISBN = "isbn";
    private static final String BOOK_SEARCH_CRITERION_YEAR = "year";
    private static final String BOOK_SEARCH_CRITERION_Author = "author";
    private static final String BOOK_SEARCH_CRITERION_Genre = "genre";

    private static final String SHOW_BOOKS = "books";
    private static final String SHOW_GenreS = "genres";
    private static final String SHOW_AuthorS = "authors";

    private final Table EMPTY_TABLE = new TableBuilder(new ArrayTableModel(new Object[0][0])).build();

    private final BookStockService bookStockService;
    private final AuthorsService authorsService;
    private final GenresService genresService;
    private final CommentariesService commentariesService;
    private final EntityFullNameService entityFullNameService;

    public ShellCommands(BookStockService bookStockService, AuthorsService authorsService, GenresService genresService,
                         CommentariesService commentariesService, EntityFullNameService entityFullNameService) {
        this.bookStockService = bookStockService;
        this.authorsService = authorsService;
        this.genresService = genresService;
        this.commentariesService = commentariesService;
        this.entityFullNameService = entityFullNameService;
    }

    @ShellMethod("Show all books, authors or genres")
    public Table show(@ShellOption(
            help = "Show " + SHOW_BOOKS + " (default), " + SHOW_AuthorS + " or " + SHOW_GenreS,
            defaultValue = SHOW_BOOKS) String what) {
        if (SHOW_BOOKS.equals(what))
            return booksListToTable(bookStockService.getAllBooks());
        else if (SHOW_AuthorS.equals(what))
            return authorsListToTable(authorsService.getAllAuthors());
        else if (SHOW_GenreS.equals(what))
            return genresListToTable(genresService.getAllGenres());

        return EMPTY_TABLE;
    }

    @ShellMethod(value = "Search books with defined criterion", key = {"search"})
    public Table searchBooks(@ShellOption(help="Criterion for search: " + BOOK_SEARCH_CRITERION_NAME + ", " +
                                                BOOK_SEARCH_CRITERION_ISBN + ", " + BOOK_SEARCH_CRITERION_YEAR + ", " +
                                                BOOK_SEARCH_CRITERION_Author + " or " + BOOK_SEARCH_CRITERION_Genre,
                                          defaultValue = BOOK_SEARCH_CRITERION_NAME)
                                    String criterion,
                                    String filter) {
        BookSearchCriterion cr = Arrays.stream(BookSearchCriterion.class.getEnumConstants())
                .filter(e -> ((Enum)e).name().equalsIgnoreCase(criterion)).findAny().orElse(null);
        if (cr == null)
            throw new IllegalArgumentException("Unknown criterion: \"" + criterion + "\". See help for command.");

        List<Book> books;
        books = bookStockService.getBookByCriterion(cr, filter);

        return booksListToTable(books);
    }

    @ShellMethod(key="addbook", value="Add book to stock")
    public String addBook(@ShellOption("--title") String name,
                          @ShellOption(defaultValue = "", help = "If 'author' not specified 'authorId' required") String author,
                          @ShellOption(defaultValue = "", help = "If 'genre' not specified 'genreId' required") String genre,
                          String isbn,
                          String publisher,
                          int year,
                          @ShellOption(defaultValue = "0", help = "If 'authorId' = 0 then 'author' required") int authorId,
                          @ShellOption(defaultValue = "0", help = "If 'genreId' = 0 then 'genre' required") int genreId) {
        if (authorId == 0 && author.equals(""))
            throw new IllegalArgumentException("Author or its Id must be specified");
        if (genreId == 0 && genre.equals(""))
            throw new IllegalArgumentException("Genre or its Id must be specified");

        Book book = new Book(name, isbn, publisher, year, new Author(authorId, author), new Genre(genreId, genre));
        bookStockService.addBook(book);
        return "Book added with new Id = " + book.getId();
    }

    @ShellMethod(key="delbook", value="Delete book from stock by its Id")
    public String deleteBook(long id) {
        if (bookStockService.deleteBook(new Book(id)))
            return "Book deleted";
        else
            return String.format("Book with Id=%d not found", id);
    }

    @ShellMethod(key="editbook", value="Modify book in stock by its Id")
    public String updateBook(long id,
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
        Genre genre = new Genre(name);
        genresService.addGenre(genre);
        return "Genre added with new Id = " + genre.getId();
    }

    @ShellMethod(key="editgenre", value="Modify genre by its Id")
    public String updateGenre(long id, String name) {
        if (genresService.updateGenre(new Genre(id, name)))
            return "Genre deleted";
        else
            return String.format("Genre with Id=%d not found", id);
    }

    @ShellMethod(key="delgenre", value="Delete genre by its Id")
    public String deleteGenre(long id) {
        if (genresService.deleteGenre(new Genre(id)))
            return "Genre deleted";
        else
            return String.format("Genre with Id=%d not found", id);
    }

    @ShellMethod(key="addauthor", value="Add new author")
    public String addAuthor(String name) {
        Author author = new Author(name);
        authorsService.addAuthor(author);
        return "Author added with new Id = " + author.getId();
    }

    @ShellMethod(key="editauthor", value="Modify author by its Id")
    public String updateAuthor(int id, String name) {
        if (authorsService.updateAuthor(new Author(id, name)))
            return "Author modified";
        else
            return String.format("Author with Id=%d not found", id);
    }

    @ShellMethod(key="delauthor", value="Delete author by its Id")
    public String deleteAuthor(long id) {
        if (authorsService.deleteAuthor(new Author(id)))
            return "Author deleted";
        else
            return String.format("Author with Id=%d not found", id);
    }

    @ShellMethod(key="comments", value="Show comments for book by book's Id")
    public String showComments(long id) {
        StringBuilder res = new StringBuilder();

        Book book = new Book(id);
        List<Commentary> commentaries = commentariesService.getAllForBook(book);
        if (commentaries.size() == 0)
            res.append("No comments");
        else {
            book = commentaries.get(0).getBook();
            res.append(commentaries.size())
                    .append(" comment(s) for ")
                    .append(entityFullNameService.getBookFullName(book))
                    .append(":\n");
        }
        for (Commentary commentary: commentaries)
            res.append(entityFullNameService.getCommentFullText(commentary))
               .append("\n");

        return res.toString();
    }

    @ShellMethod(key="addcomment", value="Add comment to the book by book's Id")
    public String addCommentary(long id, String readerName, String text) {
        Commentary commentary = new Commentary(text, OffsetDateTime.now(), readerName, new Book(id));
        commentariesService.addCommentary(commentary);
        return "Comment added with new Id = " + commentary.getId();
    }

    @ShellMethod(key="delcomment", value="Delete comment by its Id")
    public String deleteCommentary(long id) {
        if (commentariesService.deleteCommentary(new Commentary(id)))
            return "Commentary deleted";
        else
            return String.format("Commentary with Id=%d not found", id);
    }

    private Table booksListToTable(List<Book> books) {
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        header.put("name", "Title");
        header.put("author.name", "Author");
        header.put("genre.name", "Genre");
        header.put("isbn", "Isbn");
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
