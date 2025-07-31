package com.example.datamodular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example.repositories")
public class DatamodularApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatamodularApplication.class, args);
		System.out.println("Hi bro");
	}

}
