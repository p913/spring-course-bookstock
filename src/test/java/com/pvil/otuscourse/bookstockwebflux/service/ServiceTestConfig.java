package com.pvil.otuscourse.bookstockwebflux.service;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceTestConfig {
    private static final String CHANGELOGS_PACKAGE = "com.pvil.otuscourse.bookstockwebflux.changelog";

    @Bean
    public Mongock mongock(MongoClient mongoClient, @Value("${spring.data.mongodb.database}") String databaseName) {
        return new SpringMongockBuilder(mongoClient, databaseName, CHANGELOGS_PACKAGE)
                .setLockQuickConfig()
                .build();
    }

}
