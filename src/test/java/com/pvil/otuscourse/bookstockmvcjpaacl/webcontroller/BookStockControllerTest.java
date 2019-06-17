package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcjpaacl.etalondata.*;
import com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockPredefinedAuthorities;
import com.pvil.otuscourse.bookstockmvcjpaacl.security.BookStockUserDetails;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.AuthorsService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.BookStockService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.CommentariesService;
import com.pvil.otuscourse.bookstockmvcjpaacl.service.GenresService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookStockController.class)
@WithMockUser(username = "reader", authorities = {BookStockPredefinedAuthorities.ROLE_READER})
class BookStockControllerTest extends ControllerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookStockService bookStockService;

    @MockBean
    private AuthorsService authorsService;

    @MockBean
    private GenresService genresService;

    @MockBean
    private CommentariesService commentariesService;

    @Test
    @DisplayName("Получить все книги")
    public void getAllTest() throws Exception {
        when(bookStockService.getAllBooks()).thenReturn(EtalonBooksForTests.getAll());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookstock"))
                .andExpect(model().attribute("books", EtalonBooksForTests.getAll()));
    }

    @Test
    @DisplayName("Поиск книг")
    public void searchTest() throws Exception {
        String searchText = "searchtextstring";
        when(bookStockService.searchBooks(any())).thenReturn(EtalonBooksForTests.getAll());
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
        List<Commentary> comments = EtalonCommentariesForTest.getAllForBook(book);
        when(bookStockService.getBookById(book.getId())).thenReturn(Optional.of(book));
        when(commentariesService.getAllForBook(book)).thenReturn(comments);
        mockMvc.perform(get("/viewbook").param("id", String.valueOf(book.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("comments", comments));
    }

    @Test
    @DisplayName("Просмотр подробностей по несуществующей книге не должен приводить к ошибке а должен возвращать пустую книгу")
    public void getBookByNonExistentIdTest() throws Exception {
        when(bookStockService.getBookById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    @DisplayName("Запрос на редактирование книги должен предоставлять подробности по книге и всех авторов и жанры для выбора, с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void editBookTest() throws Exception {
        Book book = EtalonBooksForTests.getWithComments();

        when(bookStockService.getBookById(book.getId())).thenReturn(Optional.of(book));
        when(authorsService.getAllAuthors()).thenReturn(EtalonAuthorsForTests.getAll());
        when(genresService.getAllGenres()).thenReturn(EtalonGenresForTests.getAll());

        mockMvc.perform(get("/editbook").param("id", String.valueOf(book.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("authors", EtalonAuthorsForTests.getAll()))
                .andExpect(model().attribute("genres", EtalonGenresForTests.getAll()));
    }

    @Test
    @DisplayName("Запрос на редактирование книги без id должен предоставлять пустую книгу и всех авторов и жанры для выбора, с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void editNonExistentBookTest() throws Exception {
        when(authorsService.getAllAuthors()).thenReturn(EtalonAuthorsForTests.getAll());
        when(genresService.getAllGenres()).thenReturn(EtalonGenresForTests.getAll());

        mockMvc.perform(get("/editbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", EtalonAuthorsForTests.getAll()))
                .andExpect(model().attribute("genres", EtalonGenresForTests.getAll()));
    }

    @Test
    @DisplayName("Удаление книги с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void delBook() throws Exception {
        mockMvc.perform(get("/delbook").param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/"));
    }

    @Test
    @DisplayName("Сохранение новой книги с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void saveNewBookTest() throws Exception {
        Book book = EtalonBooksForTests.getCanBeAdded();
        long newId = 11;
        doAnswer(a -> { ((Book)a.getArgument(0)).setId(newId); return null; }).when(bookStockService).addBook(any());
        mockMvc.perform(post("/savebook")
                .param("id", "0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(String.format("title=%s&author.name=%s&genre=%s&publisher=%s&year=%d&isbn=%s",
                        URLEncoder.encode(book.getTitle(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getAuthor().getName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getGenre().getName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getPublisher(), StandardCharsets.UTF_8),
                        book.getYear(),
                        URLEncoder.encode(book.getIsbn(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", newId));
    }

    @Test
    @DisplayName("Сохранение существующей книги с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void saveExistentBookTest() throws Exception {
        Book book = EtalonBooksForTests.getExistent();
        mockMvc.perform(post("/savebook")
                .param("id", String.valueOf(book.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(String.format("title=%s&author.name=%s&genre=%s&publisher=%s&year=%d&isbn=%s",
                        URLEncoder.encode(book.getTitle(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getAuthor().getName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getGenre().getName(), StandardCharsets.UTF_8),
                        URLEncoder.encode(book.getPublisher(), StandardCharsets.UTF_8),
                        book.getYear(),
                        URLEncoder.encode(book.getIsbn(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", book.getId()));
    }

    @Test
    @DisplayName("Добавленние коментария от имени текущего зарегистрированного пользователя")
    public void addComment() throws Exception {
        Commentary comment = EtalonCommentariesForTest.getCanBeAdded();

        when(bookStockService.getBookById(comment.getBook().getId()))
                .thenReturn(Optional.of(comment.getBook()));

        mockMvc.perform(post("/addcomment")
                .with(user(new BookStockUserDetails(EtalonUsersForTests.getReferenceToReader())))
                .param("bookid", String.valueOf(comment.getBook().getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(String.format("text=%s", URLEncoder.encode(comment.getText(), StandardCharsets.UTF_8))))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", comment.getBook().getId()));
    }

    @Test
    @DisplayName("Удаление коментария с ролью кладовщика")
    @WithMockUser(username = "stockkeeper", authorities = {BookStockPredefinedAuthorities.ROLE_STOCK_KEEPER})
    public void delComment() throws Exception {
        Commentary comment = EtalonCommentariesForTest.getExistent();

        mockMvc.perform(get("/delcomment")
                .param("bookid", String.valueOf(comment.getBook().getId()))
                .param("commentid", String.valueOf(comment.getId())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlTemplate("/viewbook?id={id}", comment.getBook().getId()));
    }
}