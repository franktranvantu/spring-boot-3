package com.franktranvantu.springboot3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.franktranvantu.springboot3.dto.request.UserCreationRequest;
import com.franktranvantu.springboot3.dto.response.UserResponse;
import com.franktranvantu.springboot3.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/unit-test.properties")
class UserControllerTest {
    @Autowired
    private MockMvc underTest;
    @MockBean
    @Autowired
    private UserService userService;
    private static ObjectMapper objectMapper;
    @Value("${jwt.tokens.adminToken}")
    private String adminToken;
    @Value("${jwt.tokens.user1Token}")
    private String user1Token;
    @Value("${jwt.tokens.user2Token}")
    private String user2Token;
    @Value("${params.user1Id}")
    private String user1Id;
    @Value("${params.user2Id}")
    private String user2Id;

    @BeforeAll
    protected static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void givenValidRequest_whenCreateUser_then200() throws Exception {
        final var request = UserCreationRequest
                .builder()
                .username("user1")
                .password("pass")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        final var response = UserResponse
                .builder()
                .id("82947101-aef8-46a1-8e94-990d658a5694")
                .username("user1")
                .dob(LocalDate.of(1990, 1, 1))
                .build();
        when(userService.createUser(any())).thenReturn(response);

        underTest
            .perform(
                MockMvcRequestBuilders
                    .post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));
    }

    @Test
    void givenInvalidUsernameRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest
                .builder()
                .username("us")
                .password("pass")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
    }

    @Test
    void givenInvalidPasswordRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest
                .builder()
                .username("user1")
                .password("pas")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 4 characters"));
    }

    @Test
    void givenInvalidBirthdateRequest_whenCreateUser_then400() throws Exception {
        final var request = UserCreationRequest
                .builder()
                .username("user1")
                .password("pass")
                .dob(LocalDate.of(2020, 1, 1))
                .build();

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4101))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You must be at least 18 years old"));
    }

    @Test
    void givenAdminToken_whenGetUsers_then200() throws Exception {
        underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users")
                                .header("Authorization", adminToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

        verify(userService).getUsers();
    }

    @Test
    void givenUser1Token_whenGetUsers_then403() throws Exception {
        underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users")
                                .header("Authorization", user1Token)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You do not have permission"));

        verify(userService, never()).getUsers();
    }



    @Test
    void givenAdminToken_whenGetUser_then200() throws Exception {
        when(userService.getUser(user1Id)).thenReturn(UserResponse.builder().username("user1").build());

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users/{userId}", user1Id)
                                .header("Authorization", adminToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

        verify(userService).getUser(user1Id);
    }

    @Test
    void givenUser1Token_whenGetUser_then200() throws Exception {
        when(userService.getUser(user1Id)).thenReturn(UserResponse.builder().username("user1").build());

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users/{userId}", user1Id)
                                .header("Authorization", user1Token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000));

        verify(userService).getUser(user1Id);
    }

    @Test
    void givenUser2Token_whenGetUser_then403() throws Exception {
        when(userService.getUser(user1Id)).thenReturn(UserResponse.builder().username("user1").build());

        underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users/{userId}", user1Id)
                                .header("Authorization", user2Token)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4003));

        verify(userService).getUser(user1Id);
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