package org.example.service;

import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceManualMockTest {

    private UserRepository repository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        userService = new UserService(repository, 18);
    }

    @Test
    void add_shouldSaveUserAndReturnDto() {
        // Arrange
        UserDto input = UserDto.builder()
                .id(10L)
                .name("Diana")
                .age(19)
                .status(Status.ACTIVE)
                .build();
        UserEntity savedEntity = UserEntity.builder()
                .id(10L)
                .name("Diana")
                .age(19)
                .status(Status.ACTIVE.getValue())
                .build();
        when(repository.save(any(UserEntity.class))).thenReturn(savedEntity);
        // Act
        UserDto result = userService.add(input);
        // Assert
        assertEquals(10L, result.getId());
        assertEquals("Diana", result.getName());
        assertEquals(19, result.getAge());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(repository).save(any(UserEntity.class));
    }

    @Test
    void findById_shouldReturnUserWhenFound() {
        // Arrange
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .name("Alice")
                .age(20)
                .status(Status.ACTIVE.getValue())
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        // Act
        Optional<UserDto> result = userService.findById(1L);
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
        assertEquals(Status.ACTIVE, result.get().getStatus());
    }

    @Test
    void findById_shouldReturnEmptyWhenUserDoesNotExist() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());
        // Act
        Optional<UserDto> result = userService.findById(999L);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findActiveOver18_shouldUseConfiguredMaturityAge() {
        // Arrange
        List<UserEntity> entities = List.of(
                UserEntity.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE.getValue()).build()
        );
        when(repository.findByMinAgeAndStatus(18, Status.ACTIVE.getValue())).thenReturn(entities);
        // Act
        List<UserDto> result = userService.findActiveOver18();
        // Assert
        assertEquals(2, result.size());
        assertEquals("Alice", result.getFirst().getName());
        verify(repository).findByMinAgeAndStatus(18, Status.ACTIVE.getValue());
    }

    @Test
    void countByStatus_shouldCountAllStatuses() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(
                UserEntity.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED.getValue()).build(),
                UserEntity.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE.getValue()).build()
        ));
        // Act
        Map<Status, Long> result = userService.countByStatus();
        // Assert
        assertEquals(2L, result.get(Status.ACTIVE));
        assertEquals(1L, result.get(Status.BLOCKED));
    }

    @Test
    void averageAge_shouldReturnZeroWhenRepositoryReturnsNull() {
        // Arrange
        when(repository.findAverageAge()).thenReturn(null);
        // Act
        BigDecimal result = userService.averageAge();
        // Assert
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void averageAge_shouldRoundToThreeDecimalPlaces() {
        // Arrange
        when(repository.findAverageAge()).thenReturn(20.3333333D);
        // Act
        BigDecimal result = userService.averageAge();
        // Assert
        assertEquals(new BigDecimal("20.333"), result);
    }
}
