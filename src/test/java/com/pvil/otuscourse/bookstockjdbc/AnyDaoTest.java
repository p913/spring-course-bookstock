package com.pvil.otuscourse.bookstockjdbc;

import com.pvil.otuscourse.bookstockjdbc.dao.*;
import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.domain.Book;
import com.pvil.otuscourse.bookstockjdbc.domain.Genre;
import com.pvil.otuscourse.bookstockjdbc.domain.NamedEntityWithId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

@JdbcTest
public class AnyDaoTest {

    @TestConfiguration
    public static class TestJdbcConfiguration {
        @Autowired
        private NamedParameterJdbcOperations jdbc;

        @Bean
        public AuthorDao getAuthorDao() {
            return new AuthorDaoDb(jdbc);
        }

        @Bean
        public GenreDao getGenreDao() {
            return new GenreDaoDb(jdbc);
        }

        @Bean
        public BookDao getBookDao() {
            return new BookDaoDb(jdbc);
        }
    }

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private BookDao bookDao;

    static List<Author> predefinedAuthors = Arrays.asList(
            new Author(1, "J. K. Rowling"),
            new Author(2, "Lev Tolstoy"),
            new Author(3, "Stanislav Lem"),
            new Author(4, "A.S. Pushkin"),
            new Author(5, "S. Esenin")
    );

    static List<Genre> predefinedGenres = Arrays.asList(
            new Genre(1, "Fantasy"),
            new Genre(2, "Detective"),
            new Genre(3, "Drama"),
            new Genre(4, "Science fiction"),
            new Genre(5, "Poetry"),
            new Genre(6, "Horror")
    );

    static List<Book> predefinedBooks = Arrays.asList(
            new Book(1, "Harry Potter and the Sorcerer's Stone", "9780590353427", "Scholastic, Inc.", 1999,
                    new Author(1,"J. K. Rowling"), new Genre(1, "Fantasy")),
            new Book(2, "Harry Potter and the Goblet of Fire ", "9780545582957", "Scholastic, Inc.", 2013,
                    new Author(1, "J. K. Rowling"), new Genre(1, "Fantasy")),
            new Book(3, "Solaris", "9780156027601", "Mariner", 2002,
                    new Author(3, "Stanislav Lem"), new Genre(4, "Science fiction")),
            new Book(4, "The Invincible", "9788363471545", "Pro Auctore Wojciech Zemek", 2017,
                    new Author(3, "Stanislav Lem"), new Genre(4, "Science fiction")),
            new Book(5, "Anna Karenina", "9780679783305", "Modern Library", 2000,
                    new Author(2, "Lev Tolstoy"), new Genre(3, "Drama")),
            new Book(6, "War and Peace", "9781400079988", "Vintage", 2008,
                    new Author(2, "Lev Tolstoy"), new Genre(3, "Drama")),
            new Book(7, "Eugene Onegin: A Novel in Verse", "9780199538645", "Oxford University Press", 2009,
                    new Author(4, "A.S. Pushkin"), new Genre(5,"Poetry"))
    );

    private Map<String, NamedEntityWithIdDao> daoAliases = new HashMap<>();

    private NamedEntityWithIdDao getDaoByAlias(String alias) {
        NamedEntityWithIdDao res = daoAliases.get(alias);
        if (res == null)
            throw new RuntimeException("Dao not found for alias " + alias);
        return res;
    }

    @BeforeEach
    private void prepareDaoAliases () {
        daoAliases.put("author", authorDao);
        daoAliases.put("genre", genreDao);
        daoAliases.put("book", bookDao);
    }

    @ParameterizedTest
    @MethodSource("paramNonExisting")
    @DisplayName("Добавление сущности")
    public void addEntityTest(String daoAlias, NamedEntityWithId nonExisting) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        int res = dao.add(nonExisting);
        assertThat(res).isEqualTo(nonExisting.getId());
        nonExisting.setId(res);

        List<NamedEntityWithId> entities = dao.getByName(nonExisting.getName());

        assertThat(entities).containsOnly(nonExisting);
    }

    @ParameterizedTest
    @MethodSource("paramExisting")
    @DisplayName("Повторное добавление сущности должно вызывать исключение")
    public void addDuplicatedEntityTest(String daoAlias, NamedEntityWithId existing) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        assertThatThrownBy( () -> dao.add(existing));
    }

    @ParameterizedTest
    @MethodSource("paramExisting")
    @DisplayName("Получение сущности по id")
    public void getEntityByIdTest(String daoAlias, NamedEntityWithId existing) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);
        Optional<NamedEntityWithId> entity = dao.getById(existing.getId());

        assertThat(entity.isPresent()).isTrue();
        assertThat(entity.get()).isEqualTo(existing);
    }

    @ParameterizedTest
    @MethodSource("paramExisting")
    @DisplayName("Получение сущности по имени")
    public void getEntityByNameTest(String daoAlias, NamedEntityWithId existing) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);
        List<NamedEntityWithId> entities = dao.getByName(existing.getName());

        assertThat(entities).containsOnly(existing);
    }

    @ParameterizedTest
    @MethodSource("paramPartNameAllEntities")
    @DisplayName("Получение сущности по части имени ")
    public void getEntityByPartOfNameTest(String daoAlias, String partName, List<NamedEntityWithId> all) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);
        List<NamedEntityWithId> authors = dao.getByName(partName);

        List<NamedEntityWithId> expected = all.stream().filter(a -> a.getName().contains(partName)).collect(Collectors.toList());
        assertThat(authors).containsAll(expected);
    }

    @ParameterizedTest
    @MethodSource("paramForUpdate")
    @DisplayName("Изменение сущности")
    public void updateEntityTest(String daoAlias, NamedEntityWithId forUpdate) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        boolean res = dao.update(forUpdate);
        assertThat(res).isTrue();

        Optional<NamedEntityWithId> updated = dao.getById(forUpdate.getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get()).isEqualTo(forUpdate);
    }

    @ParameterizedTest
    @MethodSource("paramNonExisting")
    @DisplayName("Изменение несуществующей сущности")
    public void updateNonExistingEntityTest(String daoAlias, NamedEntityWithId existing) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        boolean res = dao.update(existing);
        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @MethodSource("paramWithoutReferences")
    @DisplayName("Удаления сущности")
    public void deleteEntityTest(String daoAlias, NamedEntityWithId withoutReferences) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        boolean res = dao.delete(withoutReferences);
        assertThat(res).isTrue();

        Optional<NamedEntityWithId> deleted = dao.getById(withoutReferences.getId());

        assertThat(deleted.isPresent()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("paramNonExisting")
    @DisplayName("Удаления несуществующей сущности")
    public void deleteNonExistingEntityTest(String daoAlias, NamedEntityWithId nonExisting) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        boolean res = dao.delete(nonExisting);
        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @MethodSource("paramWithReferences")
    @DisplayName("Удаления сущности, на которую ссылаются")
    public void deleteReferencedEntityTest(String daoAlias, NamedEntityWithId existing) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        assertThatThrownBy(() -> dao.delete(existing));
    }

    @ParameterizedTest
    @MethodSource("paramAll")
    @DisplayName("Выборки всех сущносте из подготовленных в ресурсах данных")
    public void getAllEntitiesTest(String daoAlias, List<NamedEntityWithId> all) {
        NamedEntityWithIdDao dao = getDaoByAlias(daoAlias);

        List<NamedEntityWithId> entities = dao.getAll();

        assertThat(entities).containsAll(all);
    }

    public static Stream<Arguments> paramNonExisting () {
        return Stream.of(
                Arguments.of("author", new Author(6, "V. Sorokin")),
                Arguments.of("genre", new Genre(7, "Life of great people")),
                Arguments.of("book", new Book(8, "Non existing", "noIsbn", "noPublisher", 0,
                        new Author(4, "A.S. Pushkin"), new Genre(5,"Poetry")))
        );
    }

    public static Stream<Arguments> paramExisting () {
        return Stream.of(
                Arguments.of("author", predefinedAuthors.get(0)),
                Arguments.of("genre", predefinedGenres.get(0)),
                Arguments.of("book", predefinedBooks.get(0))
        );
    }

    public static Stream<Arguments> paramForUpdate () {
        return Stream.of(
                Arguments.of("author", new Author(1, "New Author")),
                Arguments.of("genre", new Genre(1, "New Genre")),
                Arguments.of("book", new Book(1, "New book", "newIsbn", "newPublisher", 1900,
                        predefinedAuthors.get(0),predefinedGenres.get(0)))
        );
    }

    public static Stream<Arguments> paramPartNameAllEntities () {
        return Stream.of(
                Arguments.of("author", "n", predefinedAuthors),
                Arguments.of("genre", "e", predefinedGenres),
                Arguments.of("book", "e", predefinedBooks)
        );
    }

    public static Stream<Arguments> paramWithoutReferences () {
        return Stream.of(
                Arguments.of("author", predefinedAuthors.get(predefinedAuthors.size() - 1)),
                Arguments.of("genre", predefinedGenres.get(predefinedGenres.size() - 1)),
                Arguments.of("book", predefinedBooks.get(predefinedBooks.size() - 1))
        );
    }

    public static Stream<Arguments> paramWithReferences () {
        return Stream.of(
                Arguments.of("author", predefinedAuthors.get(0)),
                Arguments.of("genre", predefinedGenres.get(0))
        );
    }

    public static Stream<Arguments> paramAll () {
        return Stream.of(
                Arguments.of("author", predefinedAuthors),
                Arguments.of("genre", predefinedGenres),
                Arguments.of("book", predefinedBooks)
        );
    }

}
