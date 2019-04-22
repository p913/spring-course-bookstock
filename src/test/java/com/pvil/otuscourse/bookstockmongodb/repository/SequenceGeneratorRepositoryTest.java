package com.pvil.otuscourse.bookstockmongodb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class SequenceGeneratorRepositoryTest {
    private static final String SEQ_NAME = "test_id_seq";

    @Test
    public void documentMappingAndGenSeqTest(@Autowired SequenceGeneratorRepository sequenceGeneratorRepository) {
        long id1 = sequenceGeneratorRepository.generateSequence(SEQ_NAME);
        assertThat(id1).isNotZero();

        long id2 = sequenceGeneratorRepository.generateSequence(SEQ_NAME);
        assertThat(id2).isGreaterThan(id1);
    }
}