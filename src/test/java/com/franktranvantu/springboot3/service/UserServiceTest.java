package com.franktranvantu.springboot3.service;

import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.entity.Permission;
import com.franktranvantu.springboot3.entity.Role;
import com.franktranvantu.springboot3.entity.User;
import com.franktranvantu.springboot3.exception.ServiceException;
import com.franktranvantu.springboot3.mapper.UserMapper;
import com.franktranvantu.springboot3.repository.RoleRepository;
import com.franktranvantu.springboot3.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;

import static com.franktranvantu.springboot3.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("unit-test")
class UserServiceTest {
    @Autowired
    private UserService underTest;
    @Autowired
    private UserMapper userMapper;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void givenValidRequest_whenCreateUser_thenSuccess() {
        final var request = UserCreationRequest
                .builder()
                .username("user1")
                .password("pass")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        final var user = User
                .builder()
                .id("82947101-aef8-46a1-8e94-990d658a5694")
                .username("user1")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .roles(Set.of(
                        Role.builder()
                            .name("USER")
                            .permissions(Set.of(
                                Permission.builder()
                                    .name("CREATE_POST")
                                    .build()
                            ))
                        .build()
                ))
                .build();
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(roleRepository.getReferenceById(USER.name())).thenReturn(mock(Role.class));
        when(userRepository.save(any(User.class))).thenReturn(user);

        final var userResponse = underTest.createUser(request);

        verify(userRepository).existsByUsername(request.getUsername());
        verify(roleRepository).getReferenceById(USER.name());
        verify(userRepository).save(any(User.class));
        assertThat(userResponse).isEqualTo(userMapper.toUserResponse(user));
    }

    @Test
    void givenExistedUserRequest_whenCreateUser_thenFail() {
        final var request = UserCreationRequest
                .builder()
                .build();
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        final var serviceStatusCode = assertThrows(ServiceException.class, () -> underTest.createUser(request)).getServiceStatusCode();

        assertThat(serviceStatusCode.getCode()).isEqualTo(4102);
        assertThat(serviceStatusCode.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(serviceStatusCode.getMessage()).isEqualTo("The user already existed");
        verify(userRepository).existsByUsername(request.getUsername());
        verify(roleRepository, never()).getReferenceById(USER.name());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getMyInfo() {
    }
}