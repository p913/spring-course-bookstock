package com.pvil.otuscourse.bookstockmongodb.event;

import com.pvil.otuscourse.bookstockmongodb.domain.Author;
import com.pvil.otuscourse.bookstockmongodb.repository.SequenceGeneratorRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorModelListener extends AbstractMongoEventListener<Author> {
    private final SequenceGeneratorRepository sequenceGeneratorRepository;

    public AuthorModelListener(SequenceGeneratorRepository sequenceGeneratorRepository) {
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Author> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGeneratorRepository.generateSequence(Author.SEQUENCE_NAME));
        }
    }
}
