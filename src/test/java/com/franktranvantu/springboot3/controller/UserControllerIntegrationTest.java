package com.franktranvantu.springboot3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.dto.response.UserResponse;
import com.franktranvantu.springboot3.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc underTest;
    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql");
    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.show-sql", () -> true);
    }

    private static ObjectMapper objectMapper;

    @BeforeAll
    protected static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void givenValidRequest_whenCreateUser_then200() throws Exception {
        final var request = UserCreationRequest.builder()
                .username("user3")
                .password("pass")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        final var response = UserResponse.builder()
                .id("82947101-aef8-46a1-8e94-990d658a5694")
                .username("user1")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
//        when(userService.createUser(any())).thenReturn(response);

        underTest
                .perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));
    }

//    @Test
    void givenInvalidUsernameRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest.builder()
                .username("us")
                .password("pass")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

//        underTest
//                .perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
    }

//    @Test
    void givenInvalidPasswordRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest.builder()
                .username("user1")
                .password("pas")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

//        underTest
//                .perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 4 characters"));
    }

//    @Test
    void givenInvalidBirthdateRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest.builder()
                .username("user1")
                .password("pass")
                .dob(LocalDate.of(2020, 1, 1))
                .build();

//        underTest
//                .perform(MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You must be at least 18 years old"));
    }

//    @Test
//    @WithMockUser(roles = {"ADMIN"})
    void givenAdminRequest_whenGetUsers_then200() throws Exception {
//        underTest
//                .perform(MockMvcRequestBuilders.get("/users"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

//        verify(userService).getUsers();
    }

//    @Test
//    @WithMockUser()
    void givenUser_whenGetUsers_then403() throws Exception {
//        underTest
//                .perform(MockMvcRequestBuilders.get("/users"))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4003))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You do not have permission"));

//        verify(userService, never()).getUsers();
    }

//    @Test
//    @WithMockUser(roles = {"ADMIN"})
    void givenAdminRequest_whenGetUser_then200() throws Exception {
//        when(userService.getUser("user1Id"))
//                .thenReturn(UserResponse.builder().username("user1").build());

//        underTest
//                .perform(MockMvcRequestBuilders.get("/users/{userId}", "user1Id"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

//        verify(userService).getUser("user1Id");
    }

//    @Test
//    @WithMockUser(username = "user1")
    void givenUserRequest_whenGetUser_then200() throws Exception {
//        when(userService.getUser("user1Id"))
//                .thenReturn(UserResponse.builder().username("user1").build());

//        underTest
//                .perform(MockMvcRequestBuilders.get("/users/{userId}", "user1Id"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

//        verify(userService).getUser("user1Id");
    }

//    @Test
//    @WithMockUser(username = "user2")
    void givenUserRequest_whenGetUser_then403() throws Exception {
//        when(userService.getUser("user1Id"))
//                .thenReturn(UserResponse.builder().username("user1").build());

//        underTest
//                .perform(MockMvcRequestBuilders.get("/users/{userId}", "user1Id"))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4003));

//        verify(userService).getUser("user1Id");
    }
}
