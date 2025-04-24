package com.busking.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // createdAt, updatedAt 필드를 자동으로 기록
public class BuskingApplication {
	public static void main(String[] args) {
		SpringApplication.run(BuskingApplication.class, args);
	}
}