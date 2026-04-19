package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers_shouldReturnJsonArray() throws Exception {
        // Arrange
        when(userService.listAll()).thenReturn(List.of(
                UserDto.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE).build(),
                UserDto.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED).build()
        ));
        // Act
        ResultActions result = mockMvc.perform(get("/users"));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].status").value("BLOCKED"));
    }

    @Test
    void getUserById_shouldReturn404WhenUserDoesNotExist() throws Exception {
        // Arrange
        when(userService.findById(123L)).thenReturn(Optional.empty());
        // Act
        ResultActions result = mockMvc.perform(get("/users/123"));
        // Assert
        result
                .andExpect(status().isNotFound());
    }

    @Test
    void getStatusCount_shouldReturnStatistics() throws Exception {
        // Arrange
        when(userService.countByStatusAsStrings()).thenReturn(Map.of(
                "ACTIVE", 2L,
                "BLOCKED", 1L
        ));
        // Act
        ResultActions result = mockMvc.perform(get("/users/stats/status-count"));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ACTIVE").value(2))
                .andExpect(jsonPath("$.BLOCKED").value(1));
    }

    @Test
    void createUser_shouldReturn201AndCreatedUser() throws Exception {
        // Arrange
        UserDto input = UserDto.builder()
                .id(10L)
                .name("Diana")
                .age(19)
                .status(Status.ACTIVE)
                .build();
        when(userService.add(any(UserDto.class))).thenReturn(input);
        // Act
        ResultActions result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)));
        // Assert
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Diana"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getAverageAge_shouldReturnFormattedAverage() throws Exception {
        // Arrange
        when(userService.averageAge()).thenReturn(new BigDecimal("20.333"));
        // Act
        ResultActions result = mockMvc.perform(get("/users/stats/average-age"));
        // Assert
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(20.333D));
    }
}