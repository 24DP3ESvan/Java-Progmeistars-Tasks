package org.example.mapper;

import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDto_shouldConvertEntityToDto() {
        // Arrange
        UserEntity entity = UserEntity.builder()
                .id(7L)
                .name("Eva")
                .age(22)
                .status(Status.ACTIVE.getValue())
                .build();
        // Act
        UserDto dto = UserMapper.toDto(entity);
        // Assert
        assertNotNull(dto);
        assertEquals(7L, dto.getId());
        assertEquals("Eva", dto.getName());
        assertEquals(22, dto.getAge());
        assertEquals(Status.ACTIVE, dto.getStatus());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        // Arrange
        UserDto dto = UserDto.builder()
                .id(8L)
                .name("Frank")
                .age(17)
                .status(Status.BLOCKED)
                .build();
        // Act
        UserEntity entity = UserMapper.toEntity(dto);
        // Assert
        assertNotNull(entity);
        assertEquals(8L, entity.getId());
        assertEquals("Frank", entity.getName());
        assertEquals(17, entity.getAge());
        assertEquals(Status.BLOCKED.getValue(), entity.getStatus());
    }

    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        // Act
        UserDto dto = UserMapper.toDto(null);
        // Assert
        assertNull(dto);
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        // Act
        UserEntity entity = UserMapper.toEntity(null);
        // Assert
        assertNull(entity);
    }
}
