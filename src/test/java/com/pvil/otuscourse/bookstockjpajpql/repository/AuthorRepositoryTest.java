package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import com.pvil.otuscourse.bookstockjpajpql.repository.etalondata.EtalonAuthorsForTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class AuthorRepositoryTest {

    @TestConfiguration
    public static class AuthorRepositoryTestConfig {
        @Bean
        public AuthorRepository getAuthorRepository() {
            return new AuthorRepositoryJpa();
        }
    }

    @Autowired
    AuthorRepository authorRepository;

    @Test
    @DisplayName("Добавление автора")
    public void addAuthorTest() {
        Author expected = EtalonAuthorsForTests.getNonExistent();
        Author author = new Author(expected.getName());

        authorRepository.add(author);
        assertThat(author.getId()).isEqualTo(expected.getId());

        List<Author> authors = authorRepository.getByName(author.getName());
        assertThat(authors).containsOnly(author);
    }

    @Test
    @DisplayName("Повторное добавление автора должно вызывать исключение")
    public void addDuplicatedAuthorTest() {
        assertThatThrownBy( () -> authorRepository.add(EtalonAuthorsForTests.getExistent()));
    }

    @Test
    @DisplayName("Получение автора по id")
    public void getAuthorByIdTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        Optional<Author> author = authorRepository.getById(existent.getId());

        assertThat(author.isPresent()).isTrue();
        assertThat(author.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение автора по имени")
    public void getAuthorByNameTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        List<Author> authors = authorRepository.getByName(existent.getName());

        assertThat(authors).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение автора по части имени ")
    public void getAuthorByPartOfNameTest() {
        String partName = "n";
        List<Author> authors = authorRepository.getByName(partName);

        List<Author> expected = EtalonAuthorsForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(authors).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение автора")
    public void updateAuthorTest() {
        Author forUpdate = EtalonAuthorsForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = authorRepository.update(forUpdate);
        assertThat(res).isTrue();

        Optional<Author> updated = authorRepository.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей автора")
    public void updateNonExistingAuthorTest() {
        boolean res = authorRepository.update(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора")
    public void deleteAuthorTest() {
        Author withoutReferences = EtalonAuthorsForTests.getWithoutReferences();

        boolean res = authorRepository.delete(withoutReferences);
        assertThat(res).isTrue();

        Optional<Author> deleted = authorRepository.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего автора")
    public void deleteNonExistingAuthorTest() {
        boolean res = authorRepository.delete(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора, на которого ссылаются")
    public void deleteReferencedAuthorTest() {
        assertThatThrownBy(() -> authorRepository.delete(EtalonAuthorsForTests.getExistent()));
    }

    @Test
    @DisplayName("Выборки всех авторов из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Author> authors = authorRepository.getAll();

        assertThat(authors).containsAll(EtalonAuthorsForTests.getAll());
    }

}
