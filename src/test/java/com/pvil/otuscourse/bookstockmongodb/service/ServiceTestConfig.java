package com.pvil.otuscourse.bookstockmongodb.service;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({"com.pvil.otuscourse.bookstockmongodb.repository", "com.pvil.otuscourse.bookstockmongodb.events",
        "com.pvil.otuscourse.bookstockmongodb.service"})
public class ServiceTestConfig {
    private static final String CHANGELOGS_PACKAGE = "com.pvil.otuscourse.bookstockmongodb.changelog";

    @Bean
    public Mongock mongock(MongoClient mongoClient, @Value("${spring.data.mongodb.database}") String databaseName) {
        return new SpringMongockBuilder(mongoClient, databaseName, CHANGELOGS_PACKAGE)
                .setLockQuickConfig()
                .build();
    }

}
