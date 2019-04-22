package com.pvil.otuscourse.bookstockmongodb.repository;

import com.pvil.otuscourse.bookstockmongodb.domain.DatabaseSequence;
import org.springframework.data.repository.Repository;

public interface SequenceGeneratorRepository extends Repository<DatabaseSequence, String> {
    long generateSequence(String seqName);
}
