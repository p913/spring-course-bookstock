package com.pvil.otuscourse.batchpgsql2mongo.batch;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author;
import com.pvil.otuscourse.batchpgsql2mongo.repository.source.AuthorJpaRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.target.AuthorMongoRepository;
import com.pvil.otuscourse.batchpgsql2mongo.service.CachedIdStoreService;
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
public class AuthorStepConfig {

    private final CachedIdStoreService cachedIdStoreService;

    public AuthorStepConfig(CachedIdStoreService cachedIdStoreService) {
        this.cachedIdStoreService = cachedIdStoreService;
    }

    @Bean
    public ItemReader<Author> authorItemReader(AuthorJpaRepository authorJpaRepository) {
        return new RepositoryItemReaderBuilder<Author>()
                .repository(authorJpaRepository)
                .name("authorJpaItemReader")
                .sorts(new HashMap<>())
                .methodName("findAll")
                .build();
    }

    @Bean
    public ItemProcessor authorItemProcessor(Source2TargetItemConvertService source2TargetItemConvertService) {
        return (ItemProcessor<com.pvil.otuscourse.batchpgsql2mongo.domain.source.Author,
                com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author>) author ->
                    source2TargetItemConvertService.convertAuthor(author);
    }

    @Bean
    public ItemWriter<com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author>
                    authorItemWriter(AuthorMongoRepository authorMongoRepository) {
        return new RepositoryItemWriterBuilder<com.pvil.otuscourse.batchpgsql2mongo.domain.target.Author>()
                .repository(authorMongoRepository)
                .methodName("insert")
                .build();
    }

    @Bean
    public Step stepAuthors(StepBuilderFactory stepBuilderFactory, ItemWriter authorItemWriter,
                            ItemReader authorItemReader, ItemProcessor authorItemProcessor) {
        return stepBuilderFactory.get("stepAuthors")
                .chunk(5)
                .reader(authorItemReader)
                .processor(authorItemProcessor)
                .writer(authorItemWriter)
                .build();
    }

}
