package org.example.repository;

import org.example.dto.Status;
import org.example.dto.UserDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqliteUserRepository implements AutoCloseable {

    private final Connection connection;

    public SqliteUserRepository(String databaseFile) throws SQLException {
        this.connection = DriverManager.getConnection("JDBC:sqlite:" + databaseFile);
        createTableIfNeeded();
    }

    private void createTableIfNeeded() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    status TEXT NOT NULL
                )
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void save(UserDto user) throws SQLException {
        String sql = "INSERT INTO users(id, name, age, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getStatus().name());
            statement.executeUpdate();
        }
    }

    public boolean existsById(long id) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<UserDto> findAll() throws SQLException {
        String sql = "SELECT id, name, age, status FROM users ORDER BY id";
        List<UserDto> users = new ArrayList<>();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(mapRow(resultSet));
            }
        }
        return users;
    }

    public Optional<UserDto> findById(long id) throws SQLException {
        String sql = "SELECT id, name, age, status FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        }
    }

    public List<UserDto> findByMinAgeAndStatus(int minAge, Status status) throws SQLException {
        String sql = """
                SELECT id, name, age, status
                FROM users
                WHERE age >= ? AND status = ?
                ORDER BY id
                """;
        List<UserDto> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, minAge);
            statement.setString(2, status.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapRow(resultSet));
                }
            }
        }
        return users;
    }

    public Map<Status, Long> countByStatus() throws SQLException {
        String sql = """
                SELECT status, COUNT(*) AS total
                FROM users
                GROUP BY status
                ORDER BY status
                """;
        Map<Status, Long> counts = new EnumMap<>(Status.class);
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Status status = Status.valueOf(resultSet.getString("status"));
                long total = resultSet.getLong("total");
                counts.put(status, total);
            }
        }

        return counts;
    }

    public double averageAge() throws SQLException {
        String sql = "SELECT AVG(age) AS avg_age FROM users";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                double value = resultSet.getDouble("avg_age");
                return resultSet.wasNull() ? 0.0 : value;
            }
        }
        return 0.0;
    }

    public void deleteAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
        }
    }

    private UserDto mapRow(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        Status status = Status.valueOf(resultSet.getString("status"));
        return new UserDto(id, name, age, status);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
