package com.pvil.otuscourse.bookstockmongodb.shell;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import com.pvil.otuscourse.bookstockmongodb.service.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;

import java.util.*;

@ShellComponent
public class ShellCommands {
    private static final String BOOK_SEARCH_CRITERION_TITLE = "title";
    private static final String BOOK_SEARCH_CRITERION_ISBN = "isbn";
    private static final String BOOK_SEARCH_CRITERION_YEAR = "year";
    private static final String BOOK_SEARCH_CRITERION_AUTHOR = "author";

    private static final String SHOW_BOOKS = "books";
    private static final String SHOW_AUTHORS = "authors";

    private final Table EMPTY_TABLE = new TableBuilder(new ArrayTableModel(new Object[0][0])).build();

    private final BookStockService bookStockService;
    private final AuthorsService authorsService;
    private final CommentariesService commentariesService;
    private final EntityFullNameService entityFullNameService;

    public ShellCommands(BookStockService bookStockService, AuthorsService authorsService,
                         CommentariesService commentariesService, EntityFullNameService entityFullNameService) {
        this.bookStockService = bookStockService;
        this.authorsService = authorsService;
        this.commentariesService = commentariesService;
        this.entityFullNameService = entityFullNameService;
    }

    @ShellMethod("Show all books, authors or genres")
    public Table show(@ShellOption(
            help = "Show " + SHOW_BOOKS + " (default) or " + SHOW_AUTHORS,
            defaultValue = SHOW_BOOKS) String what) {
        if (SHOW_BOOKS.equals(what))
            return booksListToTable(bookStockService.getAll());
        else if (SHOW_AUTHORS.equals(what))
            return authorsListToTable(authorsService.getAll());

        return EMPTY_TABLE;
    }

    @ShellMethod(value = "Search books with defined criterion", key = {"search"})
    public Table searchBooks(@ShellOption(help="Criterion for search: " + BOOK_SEARCH_CRITERION_TITLE + ", " +
                                                BOOK_SEARCH_CRITERION_ISBN + ", " + BOOK_SEARCH_CRITERION_YEAR + " or " +
                                                BOOK_SEARCH_CRITERION_AUTHOR,
                                          defaultValue = BOOK_SEARCH_CRITERION_TITLE)
                                    String criterion,
                                    String filter) {
        BookSearchCriterion cr = Arrays.stream(BookSearchCriterion.class.getEnumConstants())
                .filter(e -> ((Enum)e).name().equalsIgnoreCase(criterion)).findAny().orElse(null);
        if (cr == null)
            throw new IllegalArgumentException("Unknown criterion: \"" + criterion + "\". See help for command.");

        List<Book> books;
        books = bookStockService.getByCriterion(cr, filter);

        return booksListToTable(books);
    }

    @ShellMethod(key="addbook", value="Add book to stock")
    public String addBook(@ShellOption("--title") String name,
                          @ShellOption(defaultValue = "", help = "If 'author' not specified 'authorId' required") String author,
                          String genre,
                          String isbn,
                          String publisher,
                          int year,
                          @ShellOption(value="author-id", defaultValue = "0", help = "If 'author-id' = 0 then 'author' required") int authorId) {
        if (authorId == 0 && author.equals(""))
            throw new IllegalArgumentException("Author or its Id must be specified");

        Book book = new Book(name, isbn, publisher, year, new Author(authorId, author), genre);
        bookStockService.add(book);
        return "Book added with new Id = " + book.getId();
    }

    @ShellMethod(key="delbook", value="Delete book from stock by its Id")
    public String deleteBook(long id) {
        if (bookStockService.delete(new Book(id)))
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

        bookStockService.update(new Book(id, name, isbn, publisher, year, new Author(authorId, author), genre));

        return "Book modified";
    }

    @ShellMethod(key="addauthor", value="Add new author")
    public String addAuthor(String name) {
        Author author = new Author(name);
        authorsService.add(author);
        return "Author added with new Id = " + author.getId();
    }

    @ShellMethod(key="editauthor", value="Modify author by its Id")
    public String updateAuthor(int id, String name) {
        if (authorsService.update(new Author(id, name)))
            return "Author modified";
        else
            return String.format("Author with Id=%d not found", id);
    }

    @ShellMethod(key="delauthor", value="Delete author by its Id")
    public String deleteAuthor(long id) {
        if (authorsService.delete(new Author(id)))
            return "Author deleted";
        else
            return String.format("Author with Id=%d not found", id);
    }

    @ShellMethod(key="comments", value="Show comments for book by book's Id")
    public String showComments(long id) {
        StringBuilder res = new StringBuilder();

        Optional<Book> book = bookStockService.getById(id);
        if (book.isEmpty())
            res.append(String.format("Book with Id=%d not found", id));
        else {
            List<Commentary> commentaries = book.get().getComments();
            if (commentaries.isEmpty())
                res.append("No");
            else
                res.append(commentaries.size());
            res.append(" comment(s) for ")
                .append(entityFullNameService.getBookFullName(book.get()))
                .append(":\n");
            for (Commentary commentary : commentaries)
                res.append(entityFullNameService.getCommentFullText(commentary))
                        .append("\n");
        }

        return res.toString();
    }

    @ShellMethod(key="addcomment", value="Add comment to the book by book's Id")
    public String addCommentary(long id, String readerName, String text) {
        Optional<Book> book = bookStockService.getById(id);
        if (book.isEmpty())
            return String.format("Book with Id=%d not found", id);

        Commentary commentary = new Commentary(text, new Date(), readerName);
        commentariesService.add(book.get(), commentary);
        return String.format("Comment added with Id=%d", commentary.getId());
    }

    @ShellMethod(key="delcomment", value="Delete comment by book's Id and comment's Id")
    public String deleteCommentary(long bookId, long commentId) {
        Optional<Book> book = bookStockService.getById(bookId);
        if (book.isEmpty())
            return String.format("Book with Id=%d not found", bookId);

        if (commentariesService.delete(book.get(), new Commentary(commentId)))
            return "Commentary deleted";
        else
            return String.format("Commentary with Id=%d not found", commentId);
    }

    private Table booksListToTable(List<Book> books) {
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        header.put("title", "Title");
        header.put("author.name", "Author");
        header.put("genre", "Genre");
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

}
