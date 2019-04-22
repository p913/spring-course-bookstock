package com.pvil.otuscourse.bookstockmongodb.event;

import com.pvil.otuscourse.bookstockmongodb.domain.Book;
import com.pvil.otuscourse.bookstockmongodb.repository.SequenceGeneratorRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class BookModelListener extends AbstractMongoEventListener<Book> {
    private final SequenceGeneratorRepository sequenceGeneratorRepository;

    public BookModelListener(SequenceGeneratorRepository sequenceGeneratorRepository) {
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGeneratorRepository.generateSequence(Book.SEQUENCE_NAME));
        }
    }
}
