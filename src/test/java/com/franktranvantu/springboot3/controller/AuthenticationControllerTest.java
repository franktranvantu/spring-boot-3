package com.franktranvantu.springboot3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franktranvantu.springboot3.dto.request.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/unit-test.properties")
class AuthenticationControllerTest {
    @Autowired
    private MockMvc underTest;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenValidRequest_whenAuthenticated_then200() throws Exception {
        final var request = AuthenticationRequest.builder()
                .username("admin")
                .password("admin")
                .build();
        underTest
                .perform(MockMvcRequestBuilders.post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(2000))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("result.authenticated").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token").isNotEmpty());
    }

    @Test
    void givenInvalidUsernameRequest_whenAuthenticated_then404() throws Exception {
        final var request =
                AuthenticationRequest.builder().username("ad").password("admin").build();
        underTest
                .perform(MockMvcRequestBuilders.post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4103))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("The user not found"));
    }

    @Test
    void givenInvalidPasswordRequest_whenAuthenticated_then401() throws Exception {
        final var request = AuthenticationRequest.builder()
                .username("admin")
                .password("pass")
                .build();
        underTest
                .perform(MockMvcRequestBuilders.post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4001))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You are not authenticated"));
    }
}
