package com.pvil.otuscourse.bookstockwebflux.restcontroller;

import com.pvil.otuscourse.bookstockwebflux.changelog.EtalonAuthorsForTests;
import com.pvil.otuscourse.bookstockwebflux.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import com.pvil.otuscourse.bookstockwebflux.service.AuthorsService;
import com.pvil.otuscourse.bookstockwebflux.service.BookStockService;
import com.pvil.otuscourse.bookstockwebflux.service.CommentariesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(BookStockController.class)
public class BookStockControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookStockService bookStockService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean
    private CommentariesService commentariesService;

    @Test
    @DisplayName("Получить всех авторов")
    public void getAuthorsTest() throws Exception {
        List<Author> authors = EtalonAuthorsForTests.getAll();

        when(authorsService.getAll())
                .thenReturn(Flux.fromIterable(authors));

        webTestClient
                .get().uri("/author/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class)
                .contains(authors.toArray(new Author[]{}));
    }

    @Test
    @DisplayName("Получить все книги")
    public void getAllBooksTest() throws Exception {
        List<Book> books = EtalonBooksForTests.getAll();

        when(bookStockService.getAll())
                .thenReturn(Flux.fromIterable(books));

        webTestClient
                .get().uri("/book/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .contains(books.toArray(new Book[]{}));
    }
    @Test
    @DisplayName("Получить книги применяя фильтр (поиск)")
    public void getBooksByFilterTest() throws Exception {
        String searchText = "что ищем";
        List<Book> books = EtalonBooksForTests.getAll();

        when(bookStockService.getContainsAnyOf(any()))
                .thenReturn(Flux.fromIterable(books));

        webTestClient
                .get().uri(uriBuilder -> uriBuilder.path("/book/")
                                .queryParam("filter", searchText)
                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .contains(books.toArray(new Book[]{}));

        verify(bookStockService).getContainsAnyOf(searchText);
    }

    @Test
    @DisplayName("Получить книгу с комментариями по id")
    public void getBookByIdWithCommentsTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();

        when(bookStockService.getById(book.getId()))
                .thenReturn(Mono.just(book));

        webTestClient
                .get()
                .uri("/book/{id}/", book.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(book);
    }

    @Test
    @DisplayName("Добавить книгу")
    public void createBookTest() throws Exception {
        Book book = EtalonBooksForTests.getCanBeAdded();

        when(bookStockService.save(any()))
                .thenReturn(Mono.just(book));

        webTestClient
                .post()
                .uri("/book/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isCreated();

        verify(bookStockService).save(book);
    }

    @Test
    @DisplayName("Обновить книгу")
    public void updateBookTest() throws Exception {
        Book book = EtalonBooksForTests.getExistent();

        when(bookStockService.save(any()))
                .thenReturn(Mono.just(book));

        webTestClient
                .put()
                .uri("/book/{id}/", book.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk();

        verify(bookStockService).save(book);
    }

    @Test
    @DisplayName("Удалить  книгу")
    public void delBookTest() throws Exception {
        String bookId = EtalonBooksForTests.getCanBeDeleted().getId();

        when(bookStockService.delete(any()))
                .thenReturn(Mono.empty().then());

        webTestClient
                .delete()
                .uri("/book/{id}/", bookId)
                .exchange()
                .expectStatus().isNoContent();

        verify(bookStockService).delete(new Book(bookId));
    }

    @Test
    @DisplayName("Получить отзывы о книге")
    public void getBookCommentsTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();

        when(commentariesService.getAllForBook(any()))
                .thenReturn(Flux.fromIterable(book.getComments()));

        webTestClient
                .get()
                .uri("/book/{id}/comment/", book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Commentary.class)
                .contains(book.getComments().toArray(new Commentary[] {}));
    }

    @Test
    @DisplayName("Добавить комментарий")
    public void addCommentTest() throws Exception {
        String bookId = EtalonBooksForTests.getExistent().getId();
        Commentary commentary = EtalonBooksForTests.getCommentCanBeAdded();

        when(commentariesService.add(any(), any()))
                .thenReturn(Mono.just(commentary));

        webTestClient
                .post()
                .uri("/book/{id}/comment/", bookId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentary), Commentary.class)
                .exchange()
                .expectStatus().isCreated();

        verify(commentariesService).add(
                eq(new Book(bookId)),
                argThat((Commentary added) ->
                    //Исключаем дату из сравнения, т.к. контроллер всегда сохраняет комментарий с новым временем
                    Objects.equals(commentary.getId(), added.getId())
                            && Objects.equals(commentary.getReader(), added.getReader())
                            && Objects.equals(commentary.getText(), added.getText())
                ));
    }

    @Test
    @DisplayName("Удалить комментарий")
    public void delCommentTest() throws Exception {
        String bookId = EtalonBooksForTests.getExistent().getId();
        String commentaryId = EtalonBooksForTests.getCommentCanBeDeleted().getId();

        when(commentariesService.delete(any(), any()))
                .thenReturn(Mono.just(true));

        webTestClient
                .delete()
                .uri("/book/{bookId}/comment/{commentId}", bookId, commentaryId)
                .exchange()
                .expectStatus().isNoContent();

        verify(commentariesService).delete(new Book(bookId), new Commentary(commentaryId));
    }
}
