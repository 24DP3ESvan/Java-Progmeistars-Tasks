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
