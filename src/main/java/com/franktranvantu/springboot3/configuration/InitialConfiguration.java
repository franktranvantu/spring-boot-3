package com.franktranvantu.springboot3.configuration;

import static com.franktranvantu.springboot3.constant.PredefinedRole.ADMIN_ROLE;
import static com.franktranvantu.springboot3.constant.PredefinedRole.USER_ROLE;

import com.franktranvantu.springboot3.entity.Permission;
import com.franktranvantu.springboot3.entity.Role;
import com.franktranvantu.springboot3.entity.User;
import com.franktranvantu.springboot3.repository.PermissionRepository;
import com.franktranvantu.springboot3.repository.RoleRepository;
import com.franktranvantu.springboot3.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class InitialConfiguration {
    @Bean
    public ApplicationRunner runner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                final var createPost = Permission.builder()
                        .name("CREATE_POST")
                        .description("The owner of this permission can create the posts")
                        .build();
                final var approvePost = Permission.builder()
                        .name("APPROVE_POST")
                        .description("The owner of this permission can approve the posts")
                        .build();
                final var rejectPost = Permission.builder()
                        .name("REJECT_POST")
                        .description("The owner of this permission can reject the posts")
                        .build();
                permissionRepository.saveAll(List.of(createPost, approvePost, rejectPost));

                final var userRole = Role.builder()
                        .name(USER_ROLE)
                        .description("The User role")
                        .permissions(Set.of(createPost))
                        .build();
                final var adminRole = Role.builder()
                        .name(ADMIN_ROLE)
                        .description("The Admin role")
                        .permissions(Set.of(createPost, approvePost, rejectPost))
                        .build();
                roleRepository.saveAll(List.of(userRole, adminRole));

                final var user1 = User.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 1")
                        .dateOfBirth(LocalDate.now())
                        .build();
                final var user2 = User.builder()
                        .username("user2")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 2")
                        .dateOfBirth(LocalDate.now())
                        .build();
                final var admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .build();
                userRepository.saveAll(List.of(user1, user2, admin));
                log.info(
                        "" + "The application was automatically init various example data for testing purpose as below:"
                                + "Permissions: CREATE_POST; APPROVE_POST; REJECT_POST"
                                + "Roles: USER had permission CREATE_POST; ADMIN had permissions CREATE_POST, APPROVE_POST, REJECT_POST"
                                + "Users: user1/pass had role USER; user2/pass had role USER, admin/admin had role ADMIN");
            }
        };
    }
}
