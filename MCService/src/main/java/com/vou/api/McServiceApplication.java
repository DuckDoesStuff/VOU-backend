package com.vou.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class McServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(McServiceApplication.class, args);
	}

}
