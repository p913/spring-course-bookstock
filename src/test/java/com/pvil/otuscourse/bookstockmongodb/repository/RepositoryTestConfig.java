package com.pvil.otuscourse.bookstockmongodb.repository;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({"com.pvil.otuscourse.bookstockmongodb.event"})
public class RepositoryTestConfig {
}
