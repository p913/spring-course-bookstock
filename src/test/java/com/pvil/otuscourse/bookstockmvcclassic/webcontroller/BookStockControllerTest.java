package com.pvil.otuscourse.bookstockmvcclassic.webcontroller;

import com.pvil.otuscourse.bookstockmvcclassic.domain.Book;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcclassic.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockmvcclassic.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcclassic.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcclassic.service.CommentariesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @DisplayName("Получить все книги")
    public void getAllTest() throws Exception {
        when(bookStockService.getAll()).thenReturn(EtalonBooksForTests.getAll());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookstock"))
                .andExpect(model().attribute("books", EtalonBooksForTests.getAll()));
    }

    @Test
    @DisplayName("Поиск книг")
    public void searchTest() throws Exception {
        String searchText = "searchtextstring";
        when(bookStockService.getContainsAnyOf(any())).thenReturn(EtalonBooksForTests.getAll());
        mockMvc.perform(get("/search").param("searchtext", searchText))
                .andExpect(status().isOk())
                .andExpect(view().name("bookstock"))
                .andExpect(model().attribute("books", EtalonBooksForTests.getAll()))
                .andExpect(model().attribute("searchtext", searchText));
    }

    @Test
    @DisplayName("Просмотр подробностей по книге, с комментариями")
    public void getBookByIdWithCommentsTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        when(bookStockService.getById(book.getId())).thenReturn(Optional.of(book));
        mockMvc.perform(get("/viewbook").param("id", book.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    @DisplayName("Просмотр подробностей по несуществующей книге не должен приводить к ошибке а должен возвращать пустую книгу")
    public void getBookByNonExistentIdTest() throws Exception {
        when(bookStockService.getById(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/viewbook").param("id", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @DisplayName("Запрос на редактирование книги должен предоставлять подробности по книге и всех авторов для выбора")
    public void editBookTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        when(bookStockService.getById(book.getId())).thenReturn(Optional.of(book));
        mockMvc.perform(get("/editbook").param("id", book.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("authors", authorsService.getAll()));
    }

    @Test
    @DisplayName("Запрос на редактирование книги без id должен предоставлять пустую книгу и всех авторов для выбора")
    public void editNonExistentBookTest() throws Exception {
        when(bookStockService.getById(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/editbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", authorsService.getAll()));
    }

    @Test
    @DisplayName("Удаление книги")
    public void delBook() throws Exception {
        mockMvc.perform(get("/delbook").param("id", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("bookstock"));
    }

    @Test
    @DisplayName("Сохранение новой книги")
    public void saveNewBookTest() throws Exception {
        Book book = EtalonBooksForTests.getCanBeAdded();
        String newId = "newId";
        doAnswer(a -> { ((Book)a.getArgument(0)).setId("newId"); return null; }).when(bookStockService).save(any());
        mockMvc.perform(post("/savebook")
                    .param("id", "")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(String.format("title=%s&author.name=%s&genre=%s&publisher=%s&year=%d&isbn=%s",
                            URLEncoder.encode(book.getTitle(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getAuthor().getName(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getGenre(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getPublisher(), StandardCharsets.UTF_8),
                            book.getYear(),
                            URLEncoder.encode(book.getIsbn(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", newId));
    }

    @Test
    @DisplayName("Сохранение существующей книги")
    public void saveExistentBookTest() throws Exception {
        Book book = EtalonBooksForTests.getExistent();
        mockMvc.perform(post("/savebook")
                    .param("id", book.getId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(String.format("title=%s&author.name=%s&genre=%s&publisher=%s&year=%d&isbn=%s",
                            URLEncoder.encode(book.getTitle(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getAuthor().getName(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getGenre(), StandardCharsets.UTF_8),
                            URLEncoder.encode(book.getPublisher(), StandardCharsets.UTF_8),
                            book.getYear(),
                            URLEncoder.encode(book.getIsbn(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }

    @Test
    @DisplayName("Добавленние коментария")
    public void addComment() throws Exception {
        Book book = EtalonBooksForTests.getExistent();
        Commentary comment = EtalonBooksForTests.getCommentCanBeAdded();

        mockMvc.perform(post("/addcomment")
                    .param("bookid", book.getId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(String.format("reader=%s&text=%s",
                            URLEncoder.encode(comment.getReader(), StandardCharsets.UTF_8),
                            URLEncoder.encode(comment.getText(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }

    @Test
    @DisplayName("Удаление коментария")
    public void delComment() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary comment = book.getComments().get(0);

        mockMvc.perform(get("/delcomment")
                    .param("bookid", book.getId())
                    .param("commentid", comment.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }
}