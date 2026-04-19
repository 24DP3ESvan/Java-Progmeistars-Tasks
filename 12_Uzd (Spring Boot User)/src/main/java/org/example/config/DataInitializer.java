package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        // Заполняем таблицу примерами только один раз.
        if (!userService.listAll().isEmpty()) {
            log.info("База уже содержит данные, начальное заполнение пропускаем");
            return;
        }
        log.info("Заполняем базу начальными данными");
        userService.add(UserDto.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE).build());
        userService.add(UserDto.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED).build());
        userService.add(UserDto.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE).build());
    }
}