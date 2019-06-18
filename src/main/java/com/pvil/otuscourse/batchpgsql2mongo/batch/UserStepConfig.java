package com.pvil.otuscourse.batchpgsql2mongo.batch;

import com.pvil.otuscourse.batchpgsql2mongo.domain.source.User;
import com.pvil.otuscourse.batchpgsql2mongo.repository.source.UserJpaRepository;
import com.pvil.otuscourse.batchpgsql2mongo.repository.target.UserMongoRepository;
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
public class UserStepConfig {
    @Bean
    public ItemReader<User> userItemReader(UserJpaRepository userJpaRepository) {
        return new RepositoryItemReaderBuilder<User>()
                .repository(userJpaRepository)
                .name("userJpaItemReader")
                .sorts(new HashMap<>())
                .methodName("findAll")
                .build();
    }

    @Bean
    public ItemProcessor userItemProcessor(Source2TargetItemConvertService source2TargetItemConvertService) {
        return (ItemProcessor<com.pvil.otuscourse.batchpgsql2mongo.domain.source.User,
                com.pvil.otuscourse.batchpgsql2mongo.domain.target.User>) user -> {
            return source2TargetItemConvertService.convertUser(user);
        };
    }

    @Bean
    public ItemWriter<com.pvil.otuscourse.batchpgsql2mongo.domain.target.User> userItemWriter(UserMongoRepository userMongoRepository) {
        return new RepositoryItemWriterBuilder<com.pvil.otuscourse.batchpgsql2mongo.domain.target.User>()
                .repository(userMongoRepository)
                .methodName("insert")
                .build();
    }

    @Bean
    public Step stepUsers(StepBuilderFactory stepBuilderFactory, ItemWriter userItemWriter,
                           ItemReader userItemReader, ItemProcessor userItemProcessor) {
        return stepBuilderFactory.get("stepUsers")
                .chunk(5)
                .reader(userItemReader)
                .processor(userItemProcessor)
                .writer(userItemWriter)
                .build();
    }

}
