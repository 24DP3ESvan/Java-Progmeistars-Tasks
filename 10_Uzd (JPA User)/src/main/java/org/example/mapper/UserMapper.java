package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.*;
import org.example.entity.UserEntity;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .status(Status.fromValue(entity.getStatus()))
                .build();
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return UserEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .status(dto.getStatus().getValue())
                .build();
    }
}