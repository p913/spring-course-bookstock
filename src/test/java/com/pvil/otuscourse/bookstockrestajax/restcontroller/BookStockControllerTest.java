package com.pvil.otuscourse.bookstockrestajax.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvil.otuscourse.bookstockrestajax.changelog.EtalonAuthorsForTests;
import com.pvil.otuscourse.bookstockrestajax.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockrestajax.domain.Author;
import com.pvil.otuscourse.bookstockrestajax.domain.Book;
import com.pvil.otuscourse.bookstockrestajax.domain.Commentary;
import com.pvil.otuscourse.bookstockrestajax.service.AuthorsService;
import com.pvil.otuscourse.bookstockrestajax.service.BookStockService;
import com.pvil.otuscourse.bookstockrestajax.service.CommentariesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.pvil.otuscourse.bookstockrestajax.restcontroller.matcher.BooksStockJsonMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookStockController.class)
public class BookStockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookStockService bookStockService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean
    private CommentariesService commentariesService;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    @DisplayName("Получить всех авторов")
    public void getAuthorsTest() throws Exception {
        List<Author> authors = EtalonAuthorsForTests.getAll();
        when(authorsService.getAll()).thenReturn(authors);
        mockMvc.perform(get("/author/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", sameAsAuthorsList(authors)));
    }

    @Test
    @DisplayName("Получить все книги")
    public void getAllBooksTest() throws Exception {
        List<Book> books = EtalonBooksForTests.getAll();
        when(bookStockService.getAll()).thenReturn(books);
        mockMvc.perform(get("/book/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", sameAsBooksListWithoutComments(books)));
    }

    @Test
    @DisplayName("Получить книги применяя фильтр (поиск)")
    public void getBooksByFilterTest() throws Exception {
        String searchText = "что ищем";
        List<Book> books = EtalonBooksForTests.getAll();
        when(bookStockService.getContainsAnyOf(any())).thenReturn(books);
        mockMvc.perform(get("/book/").param("filter", searchText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", sameAsBooksListWithoutComments(books)));
        verify(bookStockService).getContainsAnyOf(searchText);
    }

    @Test
    @DisplayName("Получить книгу с комментариями по id")
    public void getBookByIdWithCommentsTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        when(bookStockService.getById(book.getId())).thenReturn(Optional.of(book));
        mockMvc.perform(get("/book/{id}/", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(book));
    }

    @Test
    @DisplayName("Добавить книгу")
    public void createBookTest() throws Exception {
        Book book = EtalonBooksForTests.getCanBeAdded();
        mockMvc.perform(post("/book/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
        verify(bookStockService).save(book);
    }

    @Test
    @DisplayName("Обновить книгу")
    public void updateBookTest() throws Exception {
        Book book = EtalonBooksForTests.getExistent();
        mockMvc.perform(put("/book/{id}/", book.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
        verify(bookStockService).save(book);
    }

    @Test
    @DisplayName("Удалить  книгу")
    public void delBookTest() throws Exception {
        String bookId = EtalonBooksForTests.getCanBeDeleted().getId();
        mockMvc.perform(delete("/book/{id}/", bookId))
                .andExpect(status().isNoContent());
        verify(bookStockService).delete(new Book(bookId));
    }

    @Test
    @DisplayName("Получить отзывы о книге")
    public void getBookCommentsTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        when(commentariesService.getAllForBook(any())).thenReturn(book.getComments());
        mockMvc.perform(get("/book/{id}/comment/", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", sameAsCommentsList(book.getComments())));
    }

    @Test
    @DisplayName("Добавить комментарий")
    public void addCommentTest() throws Exception {
        String bookId = EtalonBooksForTests.getExistent().getId();
        Commentary commentary = EtalonBooksForTests.getCommentCanBeAdded();
        mockMvc.perform(post("/book/{id}/comment/", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(commentary)))
                .andExpect(status().isCreated());
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
        when(commentariesService.delete(any(), any())).thenReturn(true);
        mockMvc.perform(delete("/book/{bookId}/comment/{commentId}", bookId, commentaryId))
                .andExpect(status().isNoContent());
        verify(commentariesService).delete(new Book(bookId), new Commentary(commentaryId));
    }

}
