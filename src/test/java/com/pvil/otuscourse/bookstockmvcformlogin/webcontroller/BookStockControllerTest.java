package com.pvil.otuscourse.bookstockmvcformlogin.webcontroller;

import com.pvil.otuscourse.bookstockmvcformlogin.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockmvcformlogin.changelog.EtalonUsersForTests;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Book;
import com.pvil.otuscourse.bookstockmvcformlogin.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcformlogin.security.BookStockUserDetails;
import com.pvil.otuscourse.bookstockmvcformlogin.security.BookStockUserRoles;
import com.pvil.otuscourse.bookstockmvcformlogin.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcformlogin.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcformlogin.service.CommentariesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookStockController.class)
@WithMockUser(username = "reader", authorities = {BookStockUserRoles.ROLE_READER})
public class BookStockControllerTest extends ControllerBaseTest {

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
    @DisplayName("Запрос на редактирование книги должен предоставлять подробности по книге и всех авторов для выбора, с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
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
    @DisplayName("Запрос на редактирование книги без id должен предоставлять пустую книгу и всех авторов для выбора, с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
    public void editNonExistentBookTest() throws Exception {
        when(bookStockService.getById(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/editbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", authorsService.getAll()));
    }

    @Test
    @DisplayName("Запрос на редактирование книги должен быть запрещен для читателя")
    public void editBookTestExpectedForbidden() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        mockMvc.perform(get("/editbook").param("id", book.getId()))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Удаление книги с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
    public void delBook() throws Exception {
        mockMvc.perform(get("/delbook").param("id", ""))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/"));
    }

    @Test
    @DisplayName("Удаление книги должно быть запрещено для читателя")
    public void delBookExpectedForbidden() throws Exception {
        mockMvc.perform(get("/delbook").param("id", ""))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Сохранение новой книги с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
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
    @DisplayName("Сохранение существующей книги с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
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
    @DisplayName("Сохранение существующей книги должно быть запрещено для читателя")
    public void saveExistentBookExpectedForbidden() throws Exception {
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
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Добавленние коментария от имени текущего зарегистрированного пользователя")
    public void addComment() throws Exception {
        Book book = EtalonBooksForTests.getExistent();
        Commentary comment = EtalonBooksForTests.getCommentCanBeAdded();

        mockMvc.perform(post("/addcomment")
                .with(user(new BookStockUserDetails(EtalonUsersForTests.getReferenceToReader())))
                    .param("bookid", book.getId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(String.format("text=%s", URLEncoder.encode(comment.getText(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }

    @Test
    @DisplayName("Удаление коментария с ролью кладовщика")
    @WithMockUser(username = "admin", authorities = {BookStockUserRoles.ROLE_STOCK_KEEPER})
    public void delComment() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary comment = book.getComments().get(0);

        mockMvc.perform(get("/delcomment")
                    .param("bookid", book.getId())
                    .param("commentid", comment.getId()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }

    @Test
    @DisplayName("Удаление коментария должно быть запрещено для читателя")
    public void delCommentExpectedForbidden() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary comment = book.getComments().get(0);

        mockMvc.perform(get("/delcomment")
                .param("bookid", book.getId())
                .param("commentid", comment.getId()))
                .andExpect(status().isForbidden());
    }
}