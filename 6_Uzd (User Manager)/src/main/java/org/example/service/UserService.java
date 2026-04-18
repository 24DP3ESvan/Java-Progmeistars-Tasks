package org.example.service;

import org.example.dto.UserDto;
import org.example.exception.ValidationException;
import org.example.dto.Status;

import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    private final List<UserDto> users = new ArrayList<>();

    public void add(UserDto user) {
        // Базовая валидация + исключения
        if (user == null) {
            throw new ValidationException("Пользователь не может быть null");
        }
        if (user.getId() <= 0) {
            throw new ValidationException("id должен быть положительным: " + user.getId());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (user.getAge() < 0) {
            throw new ValidationException("Возраст не может быть отрицательным: " + user.getAge());
        }
        if (user.getStatus() == null) {
            throw new ValidationException("Статус не может быть null");
        }

        // Stream + lambda: проверка на дубликаты по id
        boolean exists = users.stream().anyMatch(u -> u.getId() == user.getId());
        if (exists) {
            throw new ValidationException("Пользователь с таким id уже существует: " + user.getId());
        }

        users.add(user);
    }

    public List<UserDto> listAll() {
        return Collections.unmodifiableList(users);
    }

    public List<UserDto> find(UserFilter filter) {
        // Stream API + лямбда (filter передаётся извне)
        return users.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public Map<Status, Long> countByStatus() {
        // Stream API: группировка + подсчёт
        return users.stream()
                .collect(Collectors.groupingBy(UserDto::getStatus, Collectors.counting()));
    }

    public double averageAge() {
        // Stream API: mapToInt + average
        return users.stream()
                .mapToInt(UserDto::getAge)
                .average()
                .orElse(0.0);
    }

    public Optional<UserDto> findById(long id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }
}