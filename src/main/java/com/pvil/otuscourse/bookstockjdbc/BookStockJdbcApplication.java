package com.pvil.otuscourse.bookstockjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class BookStockJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStockJdbcApplication.class, args);
	}

}
