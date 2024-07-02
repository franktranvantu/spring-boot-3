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
                var createPost = Permission.builder()
                        .name("CREATE_POST")
                        .description("The owner of this permission can create the posts")
                        .build();
                var approvePost = Permission.builder()
                        .name("APPROVE_POST")
                        .description("The owner of this permission can approve the posts")
                        .build();
                var rejectPost = Permission.builder()
                        .name("REJECT_POST")
                        .description("The owner of this permission can reject the posts")
                        .build();
                createPost = permissionRepository.save(createPost);
                approvePost = permissionRepository.save(approvePost);
                rejectPost = permissionRepository.save(rejectPost);

                var userRole = Role.builder()
                        .name(USER_ROLE)
                        .description("The User role")
                        .permissions(Set.of(createPost))
                        .build();
                var adminRole = Role.builder()
                        .name(ADMIN_ROLE)
                        .description("The Admin role")
                        .permissions(Set.of(createPost, approvePost, rejectPost))
                        .build();
                userRole = roleRepository.save(userRole);
                adminRole = roleRepository.save(adminRole);

                var user1 = User.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 1")
                        .dateOfBirth(LocalDate.now())
                        .roles(Set.of(userRole))
                        .build();
                var user2 = User.builder()
                        .username("user2")
                        .password(passwordEncoder.encode("pass"))
                        .firstName("User 2")
                        .dateOfBirth(LocalDate.now())
                        .roles(Set.of(userRole))
                        .build();
                var admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .dateOfBirth(LocalDate.now())
                        .roles(Set.of(adminRole))
                        .build();
                user1 = userRepository.save(user1);
                user2 = userRepository.save(user2);
                admin = userRepository.save(admin);

                log.info(
                        "" + "\nThe application was automatically init various example data for testing purpose as below:"
                                + "\n\tPermissions:"
                                + "\n\t\tcreatePost: {}"
                                + "\n\t\tapprovePost: {}"
                                + "\n\t\trejectPost: {}"
                                + "\n\tRoles:"
                                + "\n\t\tuserRole: {}"
                                + "\n\t\tadminRole: {}"
                                + "\n\tUsers:"
                                + "\n\t\tuser1: {}"
                                + "\n\t\tuser2: {}"
                                + "\n\t\tadmin: {}"
                        , createPost, approvePost, rejectPost, userRole, adminRole, user1, user2, admin
                );
            }
        };
    }
}
