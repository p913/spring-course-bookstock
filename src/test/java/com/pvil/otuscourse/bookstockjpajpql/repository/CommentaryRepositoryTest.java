package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Book;
import com.pvil.otuscourse.bookstockjpajpql.domain.Commentary;
import com.pvil.otuscourse.bookstockjpajpql.repository.etalondata.EtalonCommentariesForTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentaryRepositoryTest {
    @TestConfiguration
    public static class CommentaryRepositoryTestConfig {
        @Bean
        public CommentaryRepository getCommentaryRepository() {
            return new CommentaryRepositoryJpa();
        }
    }

    @Autowired
    CommentaryRepository commentaryRepository;

    @Test
    @DisplayName("Добавление комментария")
    public void addCommentaryTest() {
        Commentary expected = EtalonCommentariesForTest.getNonExistent();
        Commentary commentary = new Commentary(expected.getText(), expected.getDate(), expected.getReader(), expected.getBook());

        commentaryRepository.add(commentary);
        assertThat(commentary.getId()).isEqualTo(expected.getId());

        List<Commentary> commentaries = commentaryRepository.getAllForBook(commentary.getBook());
        assertThat(commentaries).contains(commentary);
    }

    @Test
    @DisplayName("Выборки всех комментариев для книги из подготовленных в ресурсах данных")
    public void getAllCommentariesTest() {
        Book book = new Book(7);
        List<Commentary> commentaries = commentaryRepository.getAllForBook(book);

        assertThat(commentaries).containsAll(EtalonCommentariesForTest.getAll().stream()
                .filter(a -> a.getBook().getId() == book.getId()).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Удаления комментария")
    public void deleteAuthorTest() {
        Commentary commentary = EtalonCommentariesForTest.getExistent();

        boolean res = commentaryRepository.delete(commentary);
        assertThat(res).isTrue();

        List<Commentary> remaining = commentaryRepository.getAllForBook(commentary.getBook()).stream()
                .filter(c -> c.getId() != commentary.getId()).collect(Collectors.toList());

        assertThat(remaining).doesNotContain(commentary);
    }

    @Test
    @DisplayName("Удаления несуществующего комментария")
    public void deleteNonExistingAuthorTest() {
        boolean res = commentaryRepository.delete(EtalonCommentariesForTest.getNonExistent());
        assertThat(res).isFalse();
    }

}
