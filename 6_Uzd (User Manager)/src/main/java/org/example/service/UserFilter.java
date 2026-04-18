package org.example.service;

import org.example.dto.UserDto;

@FunctionalInterface
public interface UserFilter {
    boolean test(UserDto user);
}