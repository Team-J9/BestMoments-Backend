package com.j9.bestmoments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BestMomentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestMomentsApplication.class, args);
	}

}
