package com.franktranvantu.springboot3.configuration;

import com.franktranvantu.springboot3.entity.User;
import com.franktranvantu.springboot3.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@Profile("!unit-test")
@Slf4j
public class InitialConfiguration {
    @Bean
    public ApplicationRunner runner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                final var admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .build();
                userRepository.save(admin);
                log.warn("The admin user was created automatically by the system with username and password: admin/admin. Please update the password if needed.");
            }
        };
    }
}
