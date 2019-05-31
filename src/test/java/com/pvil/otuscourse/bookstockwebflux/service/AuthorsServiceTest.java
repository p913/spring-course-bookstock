package com.pvil.otuscourse.bookstockwebflux.service;

import com.github.cloudyrock.mongock.SpringMongock;
import com.pvil.otuscourse.bookstockwebflux.changelog.EtalonAuthorsForTests;
import com.pvil.otuscourse.bookstockwebflux.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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

        StepVerifier
                .create(authorsService.save(author))
                .assertNext(a -> assertThat(author.getId()).isNotNull())
                .verifyComplete();

        StepVerifier
                .create(authorsService.getByName(author.getName()))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение автора по id")
    public void getAuthorByIdTest() {
        Author existent = EtalonAuthorsForTests.getExistent();

        StepVerifier
                .create(authorsService.getById(existent.getId()))
                .expectNext(existent)
                .verifyComplete();
    }

    @Test
    @DisplayName("Получение автора по имени")
    public void getAuthorByNameTest() {
        Author existent = EtalonAuthorsForTests.getExistent();

        StepVerifier
                .create(authorsService.getByName(existent.getName()))
                .expectNext(existent)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Получение автора по части имени ")
    public void getAuthorByPartOfNameTest() {
        String partName = "n";
        List<Author> expected = EtalonAuthorsForTests
                .getAll()
                .stream()
                .filter(a -> a.getName().contains(partName))
                .collect(Collectors.toList());

        StepVerifier
                .create(authorsService.getByPartName(partName))
                .expectNextSequence(expected)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Изменение автора")
    public void updateAuthorTest() {
        Author forUpdate = EtalonAuthorsForTests.getExistent();
        forUpdate.setName("New name");

        authorsService
                .save(forUpdate)
                .block();

        StepVerifier
                .create(authorsService.getById(forUpdate.getId()))
                .expectNext(forUpdate)
                .verifyComplete();
    }

    @Test
    @DisplayName("Удаления автора")
    public void deleteAuthorTest() {
        Author withoutReferences = EtalonAuthorsForTests.getCanBeDeleted();

        authorsService
                .delete(withoutReferences)
                .block();

        StepVerifier
                .create(authorsService.getById(withoutReferences.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Удаления автора, на которого ссылаются")
    public void deleteReferencedAuthorTest() {
        StepVerifier
                .create(authorsService.delete(EtalonAuthorsForTests.getCanNotBeDeletedDueToReferences()))
                .verifyError(EntityHasReferences.class);
    }

    @Test
    @DisplayName("Выборки всех авторов из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        StepVerifier
                .create(authorsService.getAll())
                .expectNextSequence(EtalonAuthorsForTests.getAll())
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c несуществующим id")
    public void getExistingElseCreateIfBadIdTest() {
        StepVerifier
                .create(authorsService.getExistingElseCreate(new Author("NonExistentID", "name")))
                .verifyError(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c существующим id но несовпадающим именем")
    public void getExistingElseCreateIfHasIdAndBadNameTest() {
        StepVerifier
                .create(authorsService.getExistingElseCreate(new Author(EtalonAuthorsForTests.getExistent().getId(), "name")))
                .verifyError(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Поиск/автосоздание автора по id c существующим id и совпадающим именем")
    public void getExistingElseCreateIfHasIdAndNameTest() {
        Author existent = EtalonAuthorsForTests.getExistent();

        StepVerifier
                .create(authorsService.getExistingElseCreate(existent))
                .expectNext(existent)
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск/автосоздание автора без id c несуществующим именем")
    public void getExistingElseCreateIfNoIdAndNewNameTest() {
        Author author = EtalonAuthorsForTests.getCanBeAdded();

        StepVerifier
                .create(authorsService.getExistingElseCreate(author))
                .assertNext(a -> assertThat(a.getId()).isNotNull())
                .verifyComplete();

        StepVerifier
                .create(authorsService.getByName(author.getName()))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск/автосоздание автора без id c существующим именем не создает нового")
    public void getExistingElseCreateIfNoIdAndExistingNameTest() {
        List<Author> all = authorsService
                .getAll()
                .collectList()
                .block();

        authorsService
                .getExistingElseCreate(EtalonAuthorsForTests.getExistent())
                .block();

        StepVerifier
                .create(authorsService.getAll())
                .expectNextSequence(all)
                .verifyComplete();
    }
}