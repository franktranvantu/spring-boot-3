package com.franktranvantu.springboot3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/unit-test.properties")
class SpringBoot3ApplicationTests {

    @Test
    void contextLoads() {
    }

}
