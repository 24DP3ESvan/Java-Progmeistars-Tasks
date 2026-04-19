package org.example;

import org.example.dto.*;
import org.example.exception.ValidationException;
import org.example.repository.SqliteUserRepository;
import org.example.service.UserService;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (SqliteUserRepository repository = new SqliteUserRepository("users.db")) {
            UserService service = new UserService(repository);
            try {
                service.add(new UserDto(1, "Alice", 23, Status.ACTIVE));
                service.add(new UserDto(2, "Bob", 17, Status.NEW));
                service.add(new UserDto(3, "Charlie", 40, Status.DISABLED));
                service.add(new UserDto(4, "Diana", 19, Status.ACTIVE));
            } catch (ValidationException e) {
                System.out.println("Ошибка валидации: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
                throw e;
            }
            System.out.println("Пользователи в SQLite:");
            service.listAll().forEach(System.out::println);
            System.out.println();

            List<UserDto> adultsActive = service.findActiveOver18();
            System.out.println("Взрослые ACTIVE:");
            adultsActive.forEach(System.out::println);

            Map<Status, Long> byStatus = service.countByStatus();
            System.out.println("\nКоличество по статусам:");
            byStatus.forEach((status, count) -> System.out.println(status + " = " + count));

            System.out.println("\nСредний возраст: " + service.averageAge());
            System.out.println("\nПоиск пользователя:");
            service.findById(2)
                    .ifPresentOrElse(
                            user -> System.out.println("Найден: " + user),
                            () -> System.out.println("Пользователь не найден")
                    );
            service.findById(999)
                    .ifPresentOrElse(
                            user -> System.out.println("Найден: " + user),
                            () -> System.out.println("Пользователь не найден")
                    );
        } catch (SQLException e) {
            System.out.println("Ошибка работы с SQLite: " + e.getMessage());
        }
    }
}
