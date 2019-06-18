package com.pvil.otuscourse.batchpgsql2mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BatchPgsql2mongoApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BatchPgsql2mongoApplication.class, args);

	}

}
