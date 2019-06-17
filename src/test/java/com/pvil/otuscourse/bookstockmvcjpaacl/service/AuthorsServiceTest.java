package com.pvil.otuscourse.bookstockmvcjpaacl.service;

import com.pvil.otuscourse.bookstockmvcjpaacl.domain.Author;
import com.pvil.otuscourse.bookstockmvcjpaacl.etalondata.EtalonAuthorsForTests;
import com.pvil.otuscourse.bookstockmvcjpaacl.repository.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthorsServiceTest {
    @TestConfiguration
    public static class AuthorsServiceTestConfiguration {
        @Bean
        public AuthorsService getAuthorsService(AuthorRepository authorRepository) {
            return new AuthorsServiceImpl(authorRepository);
        }
    }

    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Добавление автора")
    public void addAuthorTest() {
        Author expected = EtalonAuthorsForTests.getCanBeAdded();
        Author author = new Author(expected.getName());

        authorsService.addAuthor(author);
        assertThat(author.getId()).isNotZero();
        expected.setId(author.getId());

        List<Author> authors = authorsService.getAuthorByName(author.getName());
        assertThat(authors).containsOnly(author);
    }

    @Test
    @DisplayName("Повторное добавление автора должно вызывать исключение")
    public void addDuplicatedAuthorTest() {
        Author author = new Author(EtalonAuthorsForTests.getExistent().getName());
        assertThatThrownBy( () -> authorsService.addAuthor(author));
    }

    @Test
    @DisplayName("Получение автора по id")
    public void getAuthorByIdTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        Optional<Author> author = authorsService.getAuthorById(existent.getId());
        assertThat(author.isPresent()).isTrue();
        assertThat(author.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение автора по имени")
    public void getAuthorByNameTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        List<Author> authors = authorsService.getAuthorByName(existent.getName());

        assertThat(authors).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение автора по части имени ")
    public void getAuthorByPartOfNameTest() {
        String partName = "n";
        List<Author> authors = authorsService.getAuthorByName(partName);

        List<Author> expected = EtalonAuthorsForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(authors).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение автора")
    public void updateAuthorTest() {
        Author forUpdate = EtalonAuthorsForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = authorsService.updateAuthor(forUpdate);
        assertThat(res).isTrue();

        Optional<Author> updated = authorsService.getAuthorById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей автора")
    public void updateNonExistingAuthorTest() {
        boolean res = authorsService.updateAuthor(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора")
    public void deleteAuthorTest() {
        Author withoutReferences = EtalonAuthorsForTests.getCanBeDeleted();

        boolean res = authorsService.deleteAuthor(withoutReferences);
        assertThat(res).isTrue();

        Optional<Author> deleted = authorsService.getAuthorById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего автора")
    public void deleteNonExistingAuthorTest() {
        boolean res = authorsService.deleteAuthor(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора, на которого ссылаются")
    public void deleteReferencedAuthorTest() {
        assertThatThrownBy(() -> {
            authorsService.deleteAuthor(EtalonAuthorsForTests.getCanNotBeDeletedDueToReferences());
            //Hibernate не выполнит delete до commit транзакции, а в тесте она rollback.
            //Поэтому чтобы получить исключение по внешнему ключу вручную flush()
            testEntityManager.flush();
        });
    }

    @Test
    @DisplayName("Выборки всех авторов из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Author> authors = authorsService.getAllAuthors();

        assertThat(authors).containsAll(EtalonAuthorsForTests.getAll());
    }


}