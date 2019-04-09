package com.pvil.otuscourse.bookstockjpajpql.repository;

import com.pvil.otuscourse.bookstockjpajpql.domain.Genre;
import com.pvil.otuscourse.bookstockjpajpql.repository.etalondata.EtalonGenresForTests;
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
public class GenreRepositoryTest {

    @TestConfiguration
    public static class GenreRepositoryTestConfig {
        @Bean
        public GenreRepository getGenreRepository() {
            return new GenreRepositoryJpa();
        }
    }

    @Autowired
    GenreRepository genreRepository;

    @Test
    @DisplayName("Добавление жaнра")
    public void addGenreTest() {
        Genre expected = EtalonGenresForTests.getNonExistent();
        Genre genre = new Genre(expected.getName());

        genreRepository.add(genre);
        assertThat(genre.getId()).isEqualTo(expected.getId());

        List<Genre> genres = genreRepository.getByName(genre.getName());
        assertThat(genres).containsOnly(genre);
    }

    @Test
    @DisplayName("Повторное добавление жaнра должно вызывать исключение")
    public void addDuplicatedGenreTest() {
        assertThatThrownBy( () -> genreRepository.add(EtalonGenresForTests.getExistent()));
    }

    @Test
    @DisplayName("Получение жaнра по id")
    public void getGenreByIdTest() {
        Genre existent = EtalonGenresForTests.getExistent();
        Optional<Genre> genre = genreRepository.getById(existent.getId());

        assertThat(genre.isPresent()).isTrue();
        assertThat(genre.get()).isEqualTo(existent);
    }

    @Test
    @DisplayName("Получение жaнра по имени")
    public void getGenreByNameTest() {
        Genre existent = EtalonGenresForTests.getExistent();
        List<Genre> genres = genreRepository.getByName(existent.getName());

        assertThat(genres).containsOnly(existent);
    }

    @Test
    @DisplayName("Получение жaнра по части имени ")
    public void getGenreByPartOfNameTest() {
        String partName = "n";
        List<Genre> genres = genreRepository.getByName(partName);

        List<Genre> expected = EtalonGenresForTests.getAll().stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(genres).containsAll(expected);
    }

    @Test
    @DisplayName("Изменение жaнра")
    public void updateGenreTest() {
        Genre forUpdate = EtalonGenresForTests.getExistent();
        forUpdate.setName("New name");

        boolean res = genreRepository.update(forUpdate);
        assertThat(res).isTrue();

        Optional<Genre> updated = genreRepository.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @Test
    @DisplayName("Изменение несуществующей жaнра")
    public void updateNonExistingGenreTest() {
        boolean res = genreRepository.update(EtalonGenresForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления жaнра")
    public void deleteGenreTest() {
        Genre withoutReferences = EtalonGenresForTests.getWithoutReferences();

        boolean res = genreRepository.delete(withoutReferences);
        assertThat(res).isTrue();

        Optional<Genre> deleted = genreRepository.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Удаления несуществующего жaнра")
    public void deleteNonExistingGenreTest() {
        boolean res = genreRepository.delete(EtalonGenresForTests.getNonExistent());
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("Удаления жaнра, на которого ссылаются")
    public void deleteReferencedGenreTest() {
        assertThatThrownBy(() -> genreRepository.delete(EtalonGenresForTests.getExistent()));
    }

    @Test
    @DisplayName("Выборки всех жaнров из подготовленных в ресурсах данных")
    public void getAllEntitiesTest() {
        List<Genre> genres = genreRepository.getAll();

        assertThat(genres).containsAll(EtalonGenresForTests.getAll());
    }
}
