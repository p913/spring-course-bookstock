package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book;
import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Commentary;
import com.pvil.otuscourse.bookstockmvcjpaacl.etalondata.EtalonCommentariesForTest;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.AuthorRepository;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.BookRepository;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.CommentaryRepository;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentariesServiceTest {

    @TestConfiguration
    public static class CommentariesServiceTestConfiguration {

        @Bean
        public GenresService getGenresService(GenreRepository genresRepository) {
            return new GenresServiceImpl(genresRepository);
        }

        @Bean
        public AuthorsService getAuthorsService(AuthorRepository authorRepository) {
            return new AuthorsServiceImpl(authorRepository);
        }

        @MockBean
        public UserPermissionsForBookService userPermissionsForBookService;

        @Bean
        public BookStockService getBookStockService(BookRepository bookRepository,
                                                    AuthorsService authorsService, GenresService genresService) {
            return new BookStockServiceImpl(bookRepository, authorsService, genresService, userPermissionsForBookService);
        }

        @Bean
        public CommentariesService getCommentariesService(CommentaryRepository commentaryRepository,
                                                          BookStockService bookStockService) {
            return new CommentariesServiceImpl(commentaryRepository, bookStockService);
        }

    }

    @Autowired
    private CommentariesService commentariesService;

    @Test
    @DisplayName("Добавление комментария")
    public void addCommentaryTest() {
        Commentary expected = EtalonCommentariesForTest.getNonExistent();
        Commentary commentary = new Commentary(expected.getText(), expected.getDate(), expected.getUser(), expected.getBook());

        commentariesService.addCommentary(commentary);
        assertThat(commentary.getId()).isNotZero();
        expected.setId(commentary.getId());

        List<Commentary> commentaries = commentariesService.getAllForBook(commentary.getBook());
        assertThat(commentaries).contains(commentary);
    }

    @Test
    @DisplayName("Выборки всех комментариев для книги из подготовленных в ресурсах данных")
    public void getAllCommentariesTest() {
        Book book = new Book(7);
        List<Commentary> commentaries = commentariesService.getAllForBook(book);

        assertThat(commentaries).containsAll(EtalonCommentariesForTest.getAllForBook(book));
    }

    @Test
    @DisplayName("Удаления комментария")
    public void deleteAuthorTest() {
        Commentary commentary = EtalonCommentariesForTest.getExistent();

        boolean res = commentariesService.deleteCommentary(commentary);
        assertThat(res).isTrue();

        List<Commentary> remaining = commentariesService.getAllForBook(commentary.getBook()).stream()
                .filter(c -> c.getId() != commentary.getId()).collect(Collectors.toList());

        assertThat(remaining).doesNotContain(commentary);
    }

    @Test
    @DisplayName("Удаления несуществующего комментария")
    public void deleteNonExistingAuthorTest() {
        boolean res = commentariesService.deleteCommentary(EtalonCommentariesForTest.getNonExistent());
        assertThat(res).isFalse();
    }


}