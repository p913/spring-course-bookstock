package com.pvil.otuscourse.bookstockmongodb.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.etalondata.EtalonAuthorsForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
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
        Author expected = EtalonAuthorsForTests.getCanBeAdded();
        Author author = new Author(expected.getName());

        authorsService.add(author);
        assertThat(author.getId()).isEqualTo(expected.getId());

        List<Author> authors = authorsService.getByName(author.getName());
        assertThat(authors).containsOnly(author);
    }

    @Test
    @DisplayName("Повторное добавление существующего автора должно вызывать исключение")
    public void addDuplicatedAuthorTest() {
        Author author = new Author(EtalonAuthorsForTests.getExistent().getName());
        assertThatThrownBy( () -> authorsService.add(author));
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

        boolean res = authorsService.update(forUpdate);
        assertThat(res).isTrue();

        Optional<Author> updated = authorsService.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей автора")
    public void updateNonExistingAuthorTest() {
        boolean res = authorsService.update(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора")
    public void deleteAuthorTest() {
        Author withoutReferences = EtalonAuthorsForTests.getCanBeDeleted();

        boolean res = authorsService.delete(withoutReferences);
        assertThat(res).isTrue();

        Optional<Author> deleted = authorsService.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего автора")
    public void deleteNonExistingAuthorTest() {
        boolean res = authorsService.delete(EtalonAuthorsForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления автора, на которого ссылаются")
    public void deleteReferencedAuthorTest() {
        assertThatThrownBy(() -> {
            authorsService.delete(EtalonAuthorsForTests.getExistent());
        });
    }

    @Test
    @DisplayName("Выборки всех авторов из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Author> authors = authorsService.getAll();

        assertThat(authors).containsAll(EtalonAuthorsForTests.getAll());
    }


}