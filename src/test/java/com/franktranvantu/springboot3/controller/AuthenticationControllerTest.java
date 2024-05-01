package com.franktranvantu.springboot3.controller;

import com.franktranvantu.springboot3.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unit-test")
class AuthenticationControllerTest {
    @Autowired
    private MockMvc underTest;

    @Test
    void givenInvalidToken_whenAuthenticated_then401() {
        assertThrows(ServiceException.class, () -> underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users")
                                .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwic2NvcGUiOiJST0xFX1VTRVIgQ1JFQVRFX1BPU1QiLCJpc3MiOiJmcmFua3RyYW52YW50dS5jb20iLCJleHAiOjE3MTQ1NzEwNjgsImlhdCI6MTcxNDU2NzQ2OCwianRpIjoiMDhhMGRlMTEtNDNhNC00YjBjLWJkZWYtNTZhMjM1ZDU2OGVhIn0.rtE1Kf2drQSNxMWX0FrKbIZl6SXIOCNuDLWk2fqo6Fqn283dFyi0BUtl9q6WhSdJB-5qgd_dtPrWq5bAuyrd-A")
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(4001))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("You are not authenticated")));
    }

    @Test
    void givenUnparsedToken_whenAuthenticated_then500() {
        assertThrows(ServiceException.class, () -> underTest
                .perform(
                        MockMvcRequestBuilders
                                .get("/users")
                                .header("Authorization", "Bearer Abc")
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(9998))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("The token is invalid")));
    }

    @Test
    void refresh() {
    }

    @Test
    void introspect() {
    }

    @Test
    void logout() {
    }
}