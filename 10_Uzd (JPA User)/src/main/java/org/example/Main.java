package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderDto;
import org.example.dto.Status;
import org.example.dto.UserDto;
import org.example.repository.JpaOrderRepository;
import org.example.repository.JpaUserRepository;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

@Slf4j
public class Main {

    public static void main(String[] args) {
        configureLogging();
        log.info("Запуск приложения");

        try (JpaUserRepository repository = new JpaUserRepository()) {

            UserService userService = new UserService(repository);

            JpaOrderRepository orderRepository =
                    new JpaOrderRepository(repository.getEntityManager());

            OrderService orderService =
                    new OrderService(orderRepository, repository);

            seedData(userService, repository);
            seedOrders(orderService);

            printReport(userService, orderService);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Ошибка", e);
            throw e;
        } finally {
            log.info("Завершение приложения");
        }
    }

    private static void seedData(UserService service, JpaUserRepository repository) {
        repository.deleteAll();

        service.add(UserDto.builder().id(1L).name("Alice").age(20).status(Status.ACTIVE).build());
        service.add(UserDto.builder().id(2L).name("Bob").age(16).status(Status.BLOCKED).build());
        service.add(UserDto.builder().id(3L).name("Charlie").age(25).status(Status.ACTIVE).build());
    }

    private static void seedOrders(OrderService orderService) {
        orderService.addOrder(OrderDto.builder()
                .id(1L)
                .product("Laptop")
                .price(1200)
                .userId(1L)
                .build());

        orderService.addOrder(OrderDto.builder()
                .id(2L)
                .product("Mouse")
                .price(25)
                .userId(1L)
                .build());
    }

    private static void printReport(UserService service, OrderService orderService) {

        System.out.println("Все пользователи:");
        service.listAll().forEach(System.out::println);

        System.out.println();
        System.out.println("Активные 18+:");
        service.findActiveOver18().forEach(System.out::println);

        System.out.println();
        System.out.println("Количество по статусам:");
        System.out.println(service.countByStatus());

        System.out.println();
        System.out.println("Средний возраст:");
        System.out.println(service.averageAge());

        System.out.println();
        System.out.println("Поиск по id = 1:");
        System.out.println(service.findById(1L).orElse(null));

        System.out.println();
        System.out.println("Заказы пользователя 1:");
        orderService.getOrdersByUser(1L)
                .forEach(System.out::println);
    }

    private static void configureLogging() {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}