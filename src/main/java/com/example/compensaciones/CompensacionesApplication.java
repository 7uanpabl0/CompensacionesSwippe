package com.example.compensaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CompensacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompensacionesApplication.class, args);
	}

}
