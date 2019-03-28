package com.pvil.otuscourse.bookstockjdbc.utils;

import com.pvil.otuscourse.bookstockjdbc.dao.NamedEntityWithIdDao;
import com.pvil.otuscourse.bookstockjdbc.domain.NamedEntityWithId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityResolver {
    public static <T extends NamedEntityWithId, D extends NamedEntityWithIdDao<T>> T resolve (T entity, D dao) {
        if (entity.getId() > 0) {
            Optional<T> founded = dao.getById(entity.getId());
            if (founded.isEmpty())
                throw new EntityNotFoundException(String.format("Entity with Id = %d not found",
                        entity.getId()));
            else if (!entity.equals(founded.get()))
                throw new EntityNotFoundException(String.format("Entity with Id = %d is different entity than %s (%s)",
                        entity.getId(), entity.getName(), founded.get().getName()));
        } else {
            List<T> authors = dao.getByName(entity.getName());
            if (authors.size() == 1)
                return authors.get(0);
            else if (authors.isEmpty())
                throw new EntityNotFoundException(String.format("Entity with name \"%s\" not found", entity.getName()));
            else
                throw new TooManyEntitiesException(String.format("Too many entities found for request \"%s\": %s",
                        entity.getName(), authors.stream().map(a -> a.getName()).collect(Collectors.joining(", "))));
        }
        return entity;
    }
}
