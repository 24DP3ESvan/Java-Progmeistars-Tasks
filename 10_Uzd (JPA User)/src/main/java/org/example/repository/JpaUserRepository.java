package org.example.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Status;
import org.example.entity.UserEntity;
import java.util.*;

@Slf4j
public class JpaUserRepository implements AutoCloseable {

    private final EntityManagerFactory entityManagerFactory;
    @Getter
    private final EntityManager entityManager;

    public JpaUserRepository() {
        log.info("Инициализация JPA repository");
        this.entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public void save(UserEntity user) {
        executeInTransaction(() -> {
            log.info("Сохраняем пользователя: id={}, name={}", user.getId(), user.getName());
            entityManager.persist(user);
        }, "Ошибка при сохранении пользователя с id=" + user.getId());
    }

    public List<UserEntity> findAll() {
        log.info("Получаем всех пользователей");
        return entityManager.createQuery(
                "select u from UserEntity u order by u.id",
                UserEntity.class
        ).getResultList();
    }

    public Optional<UserEntity> findById(long id) {
        log.info("Ищем пользователя по id={}", id);
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    public List<UserEntity> findByMinAgeAndStatus(int minAge, Status status) {
        log.info("Ищем пользователей с minAge={} и status={}", minAge, status);
        return entityManager.createQuery(
                        "select u from UserEntity u where u.age >= :minAge and u.status = :status order by u.id",
                        UserEntity.class
                )
                .setParameter("minAge", minAge)
                .setParameter("status", status.getValue())
                .getResultList();
    }

    public Map<Status, Long> countByStatus() {
        log.info("Считаем количество пользователей по статусам");
        List<Object[]> rows = entityManager.createQuery(
                "select u.status, count(u) from UserEntity u group by u.status order by u.status",
                Object[].class
        ).getResultList();
        Map<Status, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Integer statusValue = (Integer) row[0];
            Long count = (Long) row[1];

            result.put(Status.fromValue(statusValue), count);
        }
        return result;
    }

    public double averageAge() {
        log.info("Считаем средний возраст пользователей");
        Double value = entityManager.createQuery(
                "select avg(u.age) from UserEntity u",
                    Double.class
                )
                .getSingleResult();
        return value != null ? value : 0.0;
    }

    public void deleteAll() {
        executeInTransaction(() -> {
            log.info("Удаляем всех пользователей");
            entityManager.createQuery("delete from UserEntity").executeUpdate();
        }, "Ошибка при удалении пользователей");
    }

    @Override
    public void close() {
        log.info("Закрываем JPA resources");
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private void executeInTransaction(Runnable action, String errorMessage) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.run();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            log.error(errorMessage, e);
            throw e;
        }
    }
}