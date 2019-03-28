package com.pvil.otuscourse.bookstockjdbc;

import com.pvil.otuscourse.bookstockjdbc.dao.AuthorDao;
import com.pvil.otuscourse.bookstockjdbc.domain.Author;
import com.pvil.otuscourse.bookstockjdbc.utils.EntityNotFoundException;
import com.pvil.otuscourse.bookstockjdbc.utils.EntityResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тест разрешения простых объектов с именем или id в полный объект, находящийся в БД")
public class EntityResolverTest {
    private Author existing = new Author(1, "Author");
    private Author nonExisting = new Author(2, "Bad Author");

    @MockBean
    private AuthorDao dao;

    @BeforeEach
    public void prepareDao() {
        when(dao.getById(existing.getId())).thenReturn(Optional.of(existing));
        when(dao.getByName(existing.getName())).thenReturn(Arrays.asList(existing));
    }

    @Test
    @DisplayName("Объект без id")
    public void ResolveByNameAuthorTest() {
        Author author = new Author(existing.getName());
        Author resolved = EntityResolver.<Author, AuthorDao>resolve(author, dao);

        assertThat(resolved).isEqualTo(existing);
    }

    @Test
    @DisplayName("Объект без id, несуществующее имя")
    public void ResolveByNameNonExistingAuthorTest() {
        Author author = new Author(nonExisting.getName());
        assertThatThrownBy(() -> EntityResolver.<Author, AuthorDao>resolve(author, dao)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Объект с id, несуществующий")
    public void ResolveNonExistingAuthorTest() {
        assertThatThrownBy(() -> EntityResolver.<Author, AuthorDao>resolve(nonExisting, dao));
    }

    @Test
    @DisplayName("Объект с id, существующий")
    public void ResolveExistingAuthorTest() {
        Author author = EntityResolver.<Author, AuthorDao>resolve(existing, dao);
        assertThat(author).isEqualTo(existing);
    }
}
