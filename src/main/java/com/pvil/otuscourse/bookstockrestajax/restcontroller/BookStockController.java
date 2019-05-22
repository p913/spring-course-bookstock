package com.pvil.otuscourse.bookstockrestajax.restcontroller;

import com.pvil.otuscourse.bookstockrestajax.domain.Author;
import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.domain.Commentary;
import com.pvil.otuscourse.bookstockrestajax.restcontroller.dto.ValidationErrors;
import com.pvil.otuscourse.bookstockrestajax.service.AuthorsService;
import com.pvil.otuscourse.bookstockrestajax.service.BookStockService;
import com.pvil.otuscourse.bookstockrestajax.service.CommentariesService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class BookStockController {
    private final BookStockService bookStockService;

    private final CommentariesService commentariesService;

    private final AuthorsService authorsService;

    public BookStockController(BookStockService bookStockService, CommentariesService commentariesService, AuthorsService authorsService) {
        this.bookStockService = bookStockService;
        this.commentariesService = commentariesService;
        this.authorsService = authorsService;
    }

    @GetMapping("/author/")
    public List<Author> getAuthors() {
        return authorsService.getAll();
    }

    @GetMapping("/book/")
    public List<Book> getBooksWithoutComments(@RequestParam(value = "filter", required = false) String filter) {
        if (filter != null)
            return bookStockService.getContainsAnyOf(filter);
        else
            return bookStockService.getAll();
    }

    @GetMapping("/book/{bookId}/")
    public Optional<Book> getBookByIdWithComments(@PathVariable("bookId") String bookId) {
        return bookStockService.getById(bookId);
    }

    @PostMapping("/book/")
    public ResponseEntity createBook(@Valid @RequestBody Book book, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ValidationErrors(errors));
        }
        else {
            bookStockService.save(book);
            return ResponseEntity.created(URI.create("/book/" + book.getId())).build();
        }
    }

    @PutMapping("/book/{bookId}/")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId, @Valid @RequestBody Book book, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ValidationErrors(errors));
        } else {
            book.setId(bookId);
            bookStockService.save(book);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/book/{bookId}/")
    public ResponseEntity delBook(@PathVariable("bookId") String bookId) {
        bookStockService.delete(new Book(bookId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{bookId}/comment/")
    public List<Commentary> getBookComments(@PathVariable("bookId") String bookId) {
        return commentariesService.getAllForBook(new Book(bookId));
    }

    @PostMapping("/book/{bookId}/comment/")
    public ResponseEntity addComment(@PathVariable("bookId") String bookId, @Valid @RequestBody Commentary commentary, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ValidationErrors(errors));
        }
        else {
            commentary.setDate(new Date());
            commentariesService.add(new Book(bookId), commentary);
            return ResponseEntity.created(URI.create("/book/" + bookId + "/comment/" + commentary.getId())).build();
        }
    }

    @DeleteMapping("/book/{bookId}/comment/{commentId}")
    public ResponseEntity delComment(@PathVariable("bookId") String bookId,
                                     @PathVariable("commentId") String commentId) {
        if (commentariesService.delete(new Book(bookId), new Commentary(commentId)))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
}
