package com.pvil.otuscourse.bookstockwebflux.restcontroller;

import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import com.pvil.otuscourse.bookstockwebflux.restcontroller.dto.FormProcessingErrors;
import com.pvil.otuscourse.bookstockwebflux.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

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
    public Flux<Author> getAuthors() {
        return authorsService.getAll();
    }

    @GetMapping("/book/")
    public Flux<Book> getBooksWithoutComments(@RequestParam(value = "filter", required = false) String filter) {
        if (filter != null)
            return bookStockService.getContainsAnyOf(filter);
        else
            return bookStockService.getAll();
    }

    @GetMapping("/book/{bookId}/")
    public Mono<Book> getBookByIdWithComments(@PathVariable("bookId") String bookId) {
        return bookStockService.getById(bookId);
    }

    @PostMapping("/book/")
    public Mono<ResponseEntity<FormProcessingErrors>> createBook(@Valid @RequestBody Book book) {
        return bookStockService.save(book).map(b -> ResponseEntity.created(URI.create("/book/" + book.getId())).build());
    }

    @PutMapping("/book/{bookId}/")
    public Mono<ResponseEntity> updateBook(@PathVariable("bookId") String bookId, @Valid @RequestBody Book book) {
        book.setId(bookId);
        return bookStockService.save(book).map(b -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/book/{bookId}/")
    public Mono<ResponseEntity> delBook(@PathVariable("bookId") String bookId) {
        return bookStockService.delete(new Book(bookId)).thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/book/{bookId}/comment/")
    public Flux<Commentary> getBookComments(@PathVariable("bookId") String bookId) {
        return commentariesService.getAllForBook(new Book(bookId));
    }

    @PostMapping("/book/{bookId}/comment/")
    public Mono<ResponseEntity<FormProcessingErrors>> addComment(@PathVariable("bookId") String bookId, @Valid @RequestBody Commentary commentary) {
        commentary.setDate(new Date());
        return commentariesService.add(new Book(bookId), commentary)
                .map(b -> ResponseEntity.created(URI.create("/book/" + bookId + "/comment/" + commentary.getId())).build());
    }

    @DeleteMapping("/book/{bookId}/comment/{commentId}")
    public Mono<ResponseEntity> delComment(@PathVariable("bookId") String bookId,
                                     @PathVariable("commentId") String commentId) {
        return commentariesService.delete(new Book(bookId), new Commentary(commentId))
            .map(d -> d ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<FormProcessingErrors> processValidationError(WebExchangeBindException ex) {
        return Mono.just(new FormProcessingErrors(ex));
    }

    @ExceptionHandler({EntityNotFoundException.class, EntityHasReferences.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<FormProcessingErrors> processEntityError(Exception ex) {
        return Mono.just(new FormProcessingErrors(ex.getMessage()));
    }

}
