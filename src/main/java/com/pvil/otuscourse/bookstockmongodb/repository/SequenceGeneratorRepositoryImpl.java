package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.DatabaseSequence;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Пользователи работают, указывая id, поэтому отказываемся от дефолтных строковых идентификаторов
 * и используем последовательности чисел.
 */
public class SequenceGeneratorRepositoryImpl implements SequenceGeneratorRepository {
    private final MongoOperations mongoOperations;

    public SequenceGeneratorRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSequence() : 1;
    }
}
