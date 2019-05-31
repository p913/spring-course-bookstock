package com.pvil.otuscourse.bookstockwebflux.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockwebflux.changelog.EtalonBooksForTests;
import com.pvil.otuscourse.bookstockwebflux.domain.Book;
import com.pvil.otuscourse.bookstockwebflux.domain.Commentary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class CommentariesServiceTest {
    @Autowired
    private CommentariesService commentariesService;

    @Autowired
    private SpringMongock mongock;

    @BeforeEach
    public void prepareDatabase() {
        mongock.execute();
    }

    @Test
    @DisplayName("Добавление комментария")
    public void addCommentaryTest() {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary expected = EtalonBooksForTests.getCommentCanBeAdded();
        Commentary commentary = new Commentary(expected.getText(), expected.getDate(), expected.getReader());

        StepVerifier
                .create(commentariesService.add(book, commentary))
                .assertNext(c -> assertThat(c.getId()).isNotNull())
                .verifyComplete();

        StepVerifier
                .create(commentariesService.getAllForBook(book).collectList())
                .assertNext(lc -> assertThat(lc).contains(commentary))
                .verifyComplete();
    }

    @Test
    @DisplayName("Выборки всех комментариев для книги из подготовленных в ресурсах данных")
    public void getAllCommentariesTest() {
        Book book = EtalonBooksForTests.getWithComments();

        StepVerifier
                .create(commentariesService.getAllForBook(book))
                .expectNextSequence(book.getComments())
                .verifyComplete();
    }

    @Test
    @DisplayName("Удаления комментария")
    public void deleteAuthorTest() {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary commentary = EtalonBooksForTests.getCommentCanBeDeleted();

        StepVerifier
                .create(commentariesService.delete(book, commentary))
                .expectNext(true)
                .verifyComplete();

        StepVerifier
                .create(commentariesService.getAllForBook(book).collectList())
                .assertNext(lc -> assertThat(lc).doesNotContain(commentary))
                .verifyComplete();
    }

}