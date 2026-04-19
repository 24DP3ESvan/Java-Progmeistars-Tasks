package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    @Value("${app.user.maturity-age:18}")
    private int maturityAge;

    public UserDto add(UserDto user) {
        log.info("Запрос на добавление пользователя: id={}, name={}", user.getId(), user.getName());
        UserEntity savedEntity = repository.save(UserMapper.toEntity(user));
        return UserMapper.toDto(savedEntity);
    }

    public List<UserDto> listAll() {
        log.info("Запрос на получение списка всех пользователей");
        return repository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserDto> findActiveOver18() {
        log.info("Запрос на получение активных пользователей {}+", maturityAge);
        return repository.findByMinAgeAndStatus(maturityAge, Status.ACTIVE.getValue())
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public Map<Status, Long> countByStatus() {
        log.info("Запрос на подсчёт пользователей по статусам");

        Map<Status, Long> result = new LinkedHashMap<>();
        result.put(Status.ACTIVE, 0L);
        result.put(Status.BLOCKED, 0L);

        for (UserEntity user : repository.findAll()) {
            Status status = Status.fromValue(user.getStatus());
            result.put(status, result.get(status) + 1);
        }

        return result;
    }

    public Map<String, Long> countByStatusAsStrings() {
        Map<String, Long> result = new LinkedHashMap<>();
        countByStatus().forEach((status, count) -> result.put(status.name(), count));
        return result;
    }

    public double averageAge() {
        log.info("Запрос на вычисление среднего возраста");
        Double value = repository.findAverageAge();
        return value != null ? value : 0.0;
    }

    public Optional<UserDto> findById(long id) {
        log.info("Запрос на поиск пользователя по id={}", id);
        return repository.findById(id).map(UserMapper::toDto);
    }
}
