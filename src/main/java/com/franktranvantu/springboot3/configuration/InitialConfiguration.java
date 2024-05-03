package com.franktranvantu.springboot3.configuration;

import com.franktranvantu.springboot3.entity.User;
import com.franktranvantu.springboot3.repository.UserRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class InitialConfiguration {
    @Bean
    public ApplicationRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                final var admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .build();
                userRepository.save(admin);
                log.info(
                        "The admin user was created automatically by the system with username and password: admin/admin. Please update the password if needed.");
            }
            if (userRepository.findUserByUsername("user1").isEmpty()) {
                final var user1 = User.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 1")
                        .dateOfBirth(LocalDate.now())
                        .build();
                userRepository.save(user1);
                log.info(
                        "The user1 user was created automatically by the system with username and password: user1/pass. Please update the password if needed.");
            }
            if (userRepository.findUserByUsername("user2").isEmpty()) {
                final var user2 = User.builder()
                        .username("user2")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 2")
                        .dateOfBirth(LocalDate.now())
                        .build();
                userRepository.save(user2);
                log.info(
                        "The user2 user was created automatically by the system with username and password: user2/pass. Please update the password if needed.");
            }
        };
    }
}
