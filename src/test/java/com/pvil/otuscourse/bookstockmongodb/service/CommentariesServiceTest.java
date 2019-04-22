package com.pvil.otuscourse.bookstockmongodb.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.domain.Commentary;
import com.pvil.otuscourse.bookstockmongodb.etalondata.EtalonBooksForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
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

        commentariesService.add(book, commentary);
        assertThat(commentary.getId()).isEqualTo(expected.getId());

        List<Commentary> commentaries = commentariesService.getAllForBook(book);
        assertThat(commentaries).contains(commentary);
    }

    @Test
    @DisplayName("Выборки всех комментариев для книги из подготовленных в ресурсах данных")
    public void getAllCommentariesTest() {
        Book book = EtalonBooksForTests.getWithComments();
        List<Commentary> commentaries = commentariesService.getAllForBook(book);

        assertThat(commentaries).containsAll(book.getComments());
    }

    @Test
    @DisplayName("Удаления комментария")
    public void deleteAuthorTest() {
        Book book = EtalonBooksForTests.getWithComments();
        Commentary commentary = EtalonBooksForTests.getCommentCanBeDeleted();

        boolean res = commentariesService.delete(book, commentary);
        assertThat(res).isTrue();

        List<Commentary> remaining = commentariesService.getAllForBook(book).stream()
                .filter(c -> c.getId() != commentary.getId()).collect(Collectors.toList());

        assertThat(remaining).doesNotContain(commentary);
    }

    @Test
    @DisplayName("Удаления несуществующего комментария")
    public void deleteNonExistingAuthorTest() {
        boolean res = commentariesService.delete(EtalonBooksForTests.getWithComments(),
                EtalonBooksForTests.getNonExistentComment());
        assertThat(res).isFalse();
    }
}