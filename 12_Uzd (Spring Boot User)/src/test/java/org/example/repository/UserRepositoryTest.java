package org.example.repository;

import org.example.dto.Status;
import org.example.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByMinAgeAndStatus_shouldReturnOrderedMatchingUsers() {
        // Arrange
        userRepository.saveAll(List.of(
                UserEntity.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED.getValue()).build(),
                UserEntity.builder().id(4L).name("Diana").age(17).status(Status.ACTIVE.getValue()).build()
        ));
        // Act
        List<UserEntity> result = userRepository.findByMinAgeAndStatus(18, Status.ACTIVE.getValue());
        // Assert
        assertEquals(2, result.size());
        assertEquals(List.of(1L, 3L), result.stream().map(UserEntity::getId).toList());
    }

    @Test
    void findAverageAge_shouldReturnAverageAcrossPersistedUsers() {
        // Arrange
        userRepository.saveAll(List.of(
                UserEntity.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE.getValue()).build(),
                UserEntity.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED.getValue()).build(),
                UserEntity.builder().id(3L).name("Charlie").age(24).status(Status.ACTIVE.getValue()).build()
        ));
        // Act
        Double averageAge = userRepository.findAverageAge();
        // Assert
        assertNotNull(averageAge);
        assertEquals(20.0, averageAge);
    }
}