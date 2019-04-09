package com.pvil.otuscourse.bookstockjpajpql.utils;

import com.pvil.otuscourse.bookstockjpajpql.domain.Author;
import com.pvil.otuscourse.bookstockjpajpql.service.BookSearchCriterion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тест разрешения через dao сущностей только с именем или с id в полную сущность")
public class EntityResolverTest {

    @Test
    @DisplayName("Объект без id")
    public void ResolveByNameEntityTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        String entity = "";
        when(entityWrapper.getEntity()).thenReturn(entity);
        when(entityDao.getBySample(any())).thenReturn(Collections.singletonList(entity));

        Object resolved = EntityResolver.resolve(entityWrapper, entityDao);

        assertThat(resolved).isEqualTo(entity);
    }

    @Test
    @DisplayName("Объект без id, несуществующее имя")
    public void ResolveByNameNonExistingEntityTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        when(entityDao.getBySample(any())).thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> EntityResolver.resolve(entityWrapper, entityDao)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Объект без id, по имени находится несколько объектов ")
    public void ResolveByNameManyEntitiesTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        when(entityWrapper.getEntity()).thenReturn("a");
        when(entityDao.getBySample(any())).thenReturn(Arrays.asList("c", "d"));

        assertThatThrownBy(() -> EntityResolver.resolve(entityWrapper, entityDao)).isInstanceOf(TooManyEntitiesException.class);
    }

    @Test
    @DisplayName("Объект с id, несуществующий")
    public void ResolveNonExistingEntityTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        when(entityWrapper.getId()).thenReturn(1L);
        when(entityDao.getById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> EntityResolver.resolve(entityWrapper, entityDao)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Объект с id и именем, существующий")
    public void ResolveExistingEntityTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        String entity = "";
        when(entityWrapper.getId()).thenReturn(1L);
        when(entityWrapper.getEntity()).thenReturn(entity);
        when(entityDao.getById(1L)).thenReturn(Optional.of(entity));

        Object resolved = EntityResolver.resolve(entityWrapper, entityDao);

        assertThat(resolved).isEqualTo(entity);
    }

    @Test
    @DisplayName("Объект с id и именем, но по id находится объект с другим именем")
    public void ResolveExistingEntityNameNotEqualTest() {
        EntityResolver.ResolvingEntityWrapper entityWrapper = mock(EntityResolver.ResolvingEntityWrapper.class);
        EntityResolver.ResolvingEntityDao entityDao = mock(EntityResolver.ResolvingEntityDao.class);

        when(entityWrapper.getId()).thenReturn(1L);
        when(entityWrapper.getEntity()).thenReturn("a");
        when(entityDao.getById(1L)).thenReturn(Optional.of("b"));

        assertThatThrownBy(() -> EntityResolver.resolve(entityWrapper, entityDao)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void test() {
        Optional<Author> op = Optional.ofNullable(null);
        System.out.println("isEmpty = " + op.isEmpty());
        System.out.println("isPresent = " + op.isPresent());
    }

}
