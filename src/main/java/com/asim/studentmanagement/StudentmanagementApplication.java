package com.asim.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class StudentmanagementApplication {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("12345"));

		SpringApplication.run(StudentmanagementApplication.class, args);
	}

}
