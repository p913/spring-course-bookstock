package com.pvil.otuscourse.bookstockdatajpa.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityResolver {
    public interface ResolvingEntityWrapper {
        long getId();
        boolean hasIdOnly();
        boolean isSame(Object to);
        Object getEntity();
    }

    public interface ResolvingEntityDao {
        Optional getById(long id);
        List getBySample(ResolvingEntityWrapper entity);
    }

    public static Object resolve (ResolvingEntityWrapper entityWrapper, ResolvingEntityDao dao) {
        if (entityWrapper.getId() > 0) {
            Optional founded = dao.getById(entityWrapper.getId());
            if (founded.isEmpty())
                throw new EntityNotFoundException(String.format("Entity with Id = %d not found",
                        entityWrapper.getId()));
            else if (entityWrapper.hasIdOnly())
                return founded.get();
            else if (!entityWrapper.isSame(founded.get()))
                throw new EntityNotFoundException(String.format("Entity with Id = %d is different entity than %s (%s)",
                        entityWrapper.getId(), entityWrapper.getEntity().toString(), founded.get().toString()));
        } else {
            List entities = dao.getBySample(entityWrapper);
            if (entities.size() == 1)
                return entities.get(0);
            else if (entities.isEmpty())
                throw new EntityNotFoundException(String.format("Entity same as \"%s\" not found", entityWrapper.toString()));
            else
                throw new TooManyEntitiesException(String.format("Too many entities same as \"%s\" found:\n%s",
                        entityWrapper.getEntity().toString(), entities.stream().map(a -> a.toString()).collect(Collectors.joining("\n"))));
        }
        return entityWrapper.getEntity();
    }
}
