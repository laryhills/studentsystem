package com.laryhills.studentsystem;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laryhills.studentsystem.utils.response.ResponseUtils;

@SpringBootApplication
@RestController
public class StudentsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentsystemApplication.class, args);
	}

	@GetMapping("/")
	public String greet() {
		return "Hello World";
	}

	@GetMapping("/api/v2")
	public ResponseEntity<Map<String, Object>> apiGreeting() {

		Map<String, Object> response = ResponseUtils.createResponse("success", "Welcome to SpringBoot Demo API v2", null);
		return ResponseEntity.badRequest().body(response);
	}

}
