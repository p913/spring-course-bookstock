package com.pvil.otuscourse.batchpgsql2mongo.batch;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.Book;
import com.pvil.otuscourse.batchpgsql2mongo.repository.source.BookJpaRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.target.BookMongoRepository;
import com.pvil.otuscourse.batchpgsql2mongo.service.Source2TargetItemConvertService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class BookStepConfig {

    @Bean
    public ItemReader<Book> bookItemReader(BookJpaRepository bookJpaRepository) {
        return new RepositoryItemReaderBuilder<Book>()
                .repository(bookJpaRepository)
                .name("bookJpaItemReader")
                .sorts(new HashMap<>())
                .methodName("findAll")
                .build();
    }

    @Bean
    public ItemProcessor bookItemProcessor(Source2TargetItemConvertService source2TargetItemConvertService) {
        return (ItemProcessor<com.pvil.otuscourse.batchpgsql2mongo.domain.source.Book,
                com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book>) book ->
                    source2TargetItemConvertService.convertBookWithComments(book);
    }

    @Bean
    public ItemWriter<com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book> bookItemWriter(BookMongoRepository bookMongoRepository) {
        return new RepositoryItemWriterBuilder<com.pvil.otuscourse.batchpgsql2mongo.domain.target.Book>()
                .repository(bookMongoRepository)
                .methodName("insert")
                .build();
    }

    @Bean
    public Step stepBooks(StepBuilderFactory stepBuilderFactory, ItemWriter bookItemWriter,
                            ItemReader bookItemReader, ItemProcessor bookItemProcessor) {
        return stepBuilderFactory.get("stepBooks")
                .chunk(5)
                .reader(bookItemReader)
                .processor(bookItemProcessor)
                .writer(bookItemWriter)
                .build();
    }

}
