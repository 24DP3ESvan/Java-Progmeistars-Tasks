package org.example.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class UserApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.saveAll(List.of(
                UserEntity.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED.getValue()).build(),
                UserEntity.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE.getValue()).build()
        ));
    }

    @Test
    void getActiveAdults_shouldReturnUsersFromDatabase() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/users/active-adults"));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Charlie"));
    }

    @Test
    void createUser_shouldPersistUserAndReturnCreatedPayload() throws Exception {
        // Arrange
        UserDto request = UserDto.builder()
                .id(10L)
                .name("Diana")
                .age(19)
                .status(Status.ACTIVE)
                .build();
        // Act
        ResultActions result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        // Assert
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Diana"))
                .andExpect(jsonPath("$.age").value(19))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        UserEntity savedUser = userRepository.findById(10L).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("Diana");
        assertThat(savedUser.getAge()).isEqualTo(19);
        assertThat(savedUser.getStatus()).isEqualTo(Status.ACTIVE.getValue());
    }

    @Test
    void getAverageAge_shouldUsePersistedUsers() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/users/stats/average-age"));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(20.333D));
    }
}
