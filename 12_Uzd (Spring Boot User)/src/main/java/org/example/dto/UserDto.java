package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private Long id;
    private String name;
    private int age;
    private Status status;
}
