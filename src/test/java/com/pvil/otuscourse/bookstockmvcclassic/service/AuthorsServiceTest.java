package com.pvil.otuscourse.bookstockmvcclassic.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockmvcclassic.domain.Author;
import com.pvil.otuscourse.bookstockmvcclassic.changelog.EtalonAuthorsForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthorsServiceTest {
    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private SpringMongock mongock;

    @BeforeEach
    public void prepareDatabase() {
        mongock.execute();
    }

    @Test
    @DisplayName("Добавление автора")
    public void addAuthorTest() {
        Author author = EtalonAuthorsForTests.getCanBeAdded();

        authorsService.save(author);
        assertThat(author.getId()).isNotNull();

        List<Author> authors = authorsService.getByName(author.getName());
        assertThat(authors).containsOnly(author);
    }

    @Test
    @DisplayName("Получение автора по id")
    public void getAuthorByIdTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        Optional<Author> author = authorsService.getById(existent.getId());
        assertThat(author.isPresent()).isTrue();
        assertThat(author.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение автора по имени")
    public void getAuthorByNameTest() {
        Author existent = EtalonAuthorsForTests.getExistent();
        List<Author> authors = authorsService.getByName(existent.getName());

        assertThat(authors).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение автора по части имени ")
    public void getAuthorByPartOfNameTest() {
        String partName = "n";
        List<Author> authors = authorsService.getByPartName(partName);

        List<Author> expected = EtalonAuthorsForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(authors).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение автора")
    public void updateAuthorTest() {
        Author forUpdate = EtalonAuthorsForTests.getExistent();
        forUpdate.setName("New name");

        authorsService.save(forUpdate);

        Optional<Author> updated = authorsService.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Удаления автора")
    public void deleteAuthorTest() {
        Author withoutReferences = EtalonAuthorsForTests.getCanBeDeleted();

        authorsService.delete(withoutReferences);

        Optional<Author> deleted = authorsService.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления автора, на которого ссылаются")
    public void deleteReferencedAuthorTest() {
        assertThatThrownBy(() -> {
            authorsService.delete(EtalonAuthorsForTests.getCanNotBeDeletedDueToReferences());
        });
    }

    @Test
    @DisplayName("Выборки всех авторов из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Author> authors = authorsService.getAll();

        assertThat(authors).containsAll(EtalonAuthorsForTests.getAll());
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c несуществующим id")
    public void getExistingElseCreateIfBadIdTest() {
        assertThatThrownBy(() -> authorsService.getExistingElseCreate(new Author("NonExistentID", "name")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c существующим id но несовпадающим именем")
    public void getExistingElseCreateIfHasIdAndBadNameTest() {
        assertThatThrownBy(() -> authorsService.getExistingElseCreate(
                                new Author(EtalonAuthorsForTests.getExistent().getId(), "name")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c существующим id и совпадающим именем")
    public void getExistingElseCreateIfHasIdAndNameTest() {
        assertThat(authorsService.getExistingElseCreate(EtalonAuthorsForTests.getExistent()))
                .isEqualTo(EtalonAuthorsForTests.getExistent());
    }

    @Test
    @DisplayName("Поиск/автосоздание автора без id c несуществующим именем")
    public void getExistingElseCreateIfNoIdAndNewNameTest() {
        Author author = authorsService.getExistingElseCreate(EtalonAuthorsForTests.getCanBeAdded());
        assertThat(author.getId()).isNotNull();
        assertThat(authorsService.getByName(author.getName())).containsOnly(author);
    }

    @Test
    @DisplayName("Поиск/автосоздание автора без id c существующим именем не создает нового")
    public void getExistingElseCreateIfNoIdAndExistingNameTest() {
        int sizeBefore = authorsService.getAll().size();
        authorsService.getExistingElseCreate(EtalonAuthorsForTests.getExistent());
        assertThat(authorsService.getAll().size()).isEqualTo(sizeBefore);
    }

}