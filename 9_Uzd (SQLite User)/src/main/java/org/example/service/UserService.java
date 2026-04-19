package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.exception.ValidationException;
import org.example.repository.SqliteUserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {

    private final SqliteUserRepository repository;

    public UserService(SqliteUserRepository repository) {
        this.repository = repository;
    }

    public void add(UserDto user) {

        try {
            if (repository.existsById(user.getId())) {
                throw new ValidationException("Пользователь с таким id уже существует: " + user.getId());
            }

            repository.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения пользователя в SQLite", e);
        }
    }

    public List<UserDto> listAll() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения пользователей из SQLite", e);
        }
    }

    public List<UserDto> findActiveOver18() {
        try {
            return repository.findByMinAgeAndStatus(18, Status.ACTIVE);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска взрослых активных пользователей", e);
        }
    }

    public Map<Status, Long> countByStatus() {
        try {
            return repository.countByStatus();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подсчёта пользователей по статусам", e);
        }
    }

    public double averageAge() {
        try {
            return repository.averageAge();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вычисления среднего возраста", e);
        }
    }

    public Optional<UserDto> findById(long id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя в SQLite", e);
        }
    }
}