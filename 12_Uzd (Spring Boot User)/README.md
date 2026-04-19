# Spring REST User Project (SQLite)


## Архитектура

- `controller` — принимает HTTP-запросы
- `service` — бизнес-логика
- `repository` — доступ к БД
- `entity` — сущность таблицы
- `dto` — объект для REST-ответов и REST-запросов
- `mapper` — преобразование между `entity` и `dto`

## Как запускать

```bash
mvn spring-boot:run
```

или

```bash
mvn clean package
java -jar target/spring-rest-user-project-sqlite-1.0-SNAPSHOT.jar
```

Порог совершеннолетия настраивается в `src/main/resources/application.properties` через свойство `app.user.maturity-age`.

Swagger UI доступен после запуска приложения по адресу `http://localhost:8080/swagger-ui.html`.
OpenAPI JSON доступен по адресу `http://localhost:8080/v3/api-docs`.

## Полезные запросы

### Получить всех пользователей

```bash
curl http://localhost:8080/users
```

### Получить пользователя по id

```bash
curl http://localhost:8080/users/1
```

### Получить активных взрослых

```bash
curl http://localhost:8080/users/active-adults
```

### Получить количество по статусам

```bash
curl http://localhost:8080/users/stats/status-count
```

### Получить средний возраст

```bash
curl http://localhost:8080/users/stats/average-age
```

### Создать пользователя

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "id": 10,
    "name": "Diana",
    "age": 19,
    "status": "ACTIVE"
  }'
```


## Что изменено для темы Unit testing / JUnit / Mockito

Проект немного зарефакторен, чтобы его было проще тестировать без поднятия всего Spring-контекста.

### Главная идея рефакторинга

`UserService` теперь получает `maturityAge` через конструктор. Это удобно для unit-тестов: можно создать сервис вручную и передать тестовое значение, не поднимая `application.properties` и весь Spring.

### Добавленные тесты

- `UserMapperTest` — обычный unit test без Mockito:
  - проверяет преобразование `Entity -> DTO`
  - проверяет преобразование `DTO -> Entity`
  - проверяет поведение на `null`

- `UserServiceTest` — unit test с Mockito:
  - мокается `UserRepository`
  - проверяется логика `add`
  - проверяется `findById`
  - проверяется `findActiveOver18`
  - проверяется `countByStatus`
  - проверяется случай, когда `averageAge()` получает `null`

- `UserControllerTest` — тест контроллера:
  - мокается `UserService`
  - используется `MockMvcBuilders.standaloneSetup(...)`
  - проверяются HTTP status и JSON-ответы

## Как запускать тесты

```bash
mvn test
```

## Что можно показать детям на уроке

1. **Что такое unit test**
   - тестируем один класс изолированно
   - не ходим в реальную базу
   - не поднимаем весь сервер

2. **Что такое mock**
   - фейковая зависимость
   - вместо настоящего `UserRepository` подставляем управляемый объект

3. **Что именно мы проверяем**
   - входные данные
   - результат работы метода
   - было ли обращение к зависимости (`verify(...)`)

4. **Чем отличаются тесты уровней**
   - `UserMapperTest` — чистая логика без моков
   - `UserServiceTest` — логика сервиса + мок репозитория
   - `UserControllerTest` — HTTP-слой без запуска всего приложения
