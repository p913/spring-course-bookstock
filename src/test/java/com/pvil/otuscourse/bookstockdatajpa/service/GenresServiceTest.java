package com.pvil.otuscourse.bookstockdatajpa.service;

import com.pvil.otuscourse.bookstockdatajpa.domain.Genre;
import com.pvil.otuscourse.bookstockdatajpa.etalondata.EtalonGenresForTests;
import com.pvil.otuscourse.bookstockdatajpa.repository.GenreRepository;
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
class GenresServiceTest {
    @TestConfiguration
    public static class GenresServiceTestConfiguration {
        @Bean
        public GenresService getaGenresService(GenreRepository genresService) {
            return new GenresServiceImpl(genresService);
        }
    }

    @Autowired
    private GenresService genresService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Добавление жaнра")
    public void addGenreTest() {
        Genre expected = EtalonGenresForTests.getNonExistent();
        Genre genre = new Genre(expected.getName());

        genresService.addGenre(genre);
        assertThat(genre.getId()).isEqualTo(expected.getId());

        List<Genre> genres = genresService.getGenreByName(genre.getName());
        assertThat(genres).containsOnly(genre);
    }

    @Test
    @DisplayName("Повторное добавление жaнра должно вызывать исключение")
    public void addDuplicatedGenreTest() {
        Genre genre = new Genre(EtalonGenresForTests.getExistent().getName());
        assertThatThrownBy( () -> genresService.addGenre(genre));
    }

    @Test
    @DisplayName("Получение жaнра по id")
    public void getGenreByIdTest() {
        Genre existent = EtalonGenresForTests.getExistent();
        Optional<Genre> genre = genresService.getGenreById(existent.getId());

        assertThat(genre.isPresent()).isTrue();
        assertThat(genre.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение жaнра по имени")
    public void getGenreByNameTest() {
        Genre existent = EtalonGenresForTests.getExistent();
        List<Genre> genres = genresService.getGenreByName(existent.getName());

        assertThat(genres).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение жaнра по части имени ")
    public void getGenreByPartOfNameTest() {
        String partName = "n";
        List<Genre> genres = genresService.getGenreByName(partName);

        List<Genre> expected = EtalonGenresForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(genres).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение жaнра")
    public void updateGenreTest() {
        Genre forUpdate = EtalonGenresForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = genresService.updateGenre(forUpdate);
        assertThat(res).isTrue();

        Optional<Genre> updated = genresService.getGenreById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей жaнра")
    public void updateNonExistingGenreTest() {
        boolean res = genresService.updateGenre(EtalonGenresForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления жaнра")
    public void deleteGenreTest() {
        Genre withoutReferences = EtalonGenresForTests.getWithoutReferences();

        boolean res = genresService.deleteGenre(withoutReferences);
        assertThat(res).isTrue();

        Optional<Genre> deleted = genresService.getGenreById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего жaнра")
    public void deleteNonExistingGenreTest() {
        boolean res = genresService.deleteGenre(EtalonGenresForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления жaнра, на которого ссылаются")
    public void deleteReferencedGenreTest() {
        assertThatThrownBy(() -> {
            genresService.deleteGenre(EtalonGenresForTests.getExistent());
            testEntityManager.flush(); //аналгично авторам - Hibernate выполнит delete
        });
    }

    @Test
    @DisplayName("Выборки всех жaнров из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Genre> genres = genresService.getAllGenres();

        assertThat(genres).containsAll(EtalonGenresForTests.getAll());
    }


}