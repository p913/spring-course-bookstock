package com.pvil.otuscourse.batchpgsql2mongo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class JobConfig {
    @Bean
    public Job importUserJob(JobBuilderFactory jobBuilderFactory,
                             Step stepUsers, Step stepAuthors, Step stepBooks) {
        return jobBuilderFactory.get("migrateToMongoDb")
                //.incrementer(new RunIdIncrementer())
                .flow(stepUsers)
                .next(stepAuthors)
                .next(stepBooks)
                .end()
                .build();
    }

}
