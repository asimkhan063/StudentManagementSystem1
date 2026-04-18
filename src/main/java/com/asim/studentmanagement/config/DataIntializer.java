package com.asim.studentmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.asim.studentmanagement.model.Users;
import com.asim.studentmanagement.repository.UsersRepository;

@Configuration
public class DataIntializer {

    @Bean
    CommandLineRunner loadSampleData(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (!usersRepository.existsByUsername("Asim"))
            {

                Users users = new Users();
                users.setUsername("Asim");
                users.setPassword(passwordEncoder.encode("12345"));
                users.setActive(true);

                usersRepository.save(users);
            }

        };
    }
}