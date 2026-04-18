package org.example.service;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.mapper.UserMapper;
import org.example.repository.JpaUserRepository;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository repository;

    public void add(UserDto user) {
        log.info("Запрос на добавление пользователя: id={}, name={}", user.getId(), user.getName());
        repository.save(UserMapper.toEntity(user));
    }

    public List<UserDto> listAll() {
        log.info("Запрос на получение списка всех пользователей");
        return repository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserDto> findActiveOver18() {
        log.info("Запрос на получение активных пользователей 18+");
        return repository.findByMinAgeAndStatus(18, Status.ACTIVE)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public Map<Status, Long> countByStatus() {
        log.info("Запрос на подсчёт пользователей по статусам");
        return repository.countByStatus();
    }

    public double averageAge() {
        log.info("Запрос на вычисление среднего возраста");
        return repository.averageAge();
    }

    public Optional<UserDto> findById(long id) {
        log.info("Запрос на поиск пользователя по id={}", id);
        return repository.findById(id).map(UserMapper::toDto);
    }
}