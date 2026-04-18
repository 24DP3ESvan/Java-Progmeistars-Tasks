package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.OrderEntity;

import java.util.List;

@Slf4j
public class JpaOrderRepository {

    private final EntityManager entityManager;

    public JpaOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(OrderEntity order) {
        executeInTransaction(() -> entityManager.persist(order));
    }

    private void executeInTransaction(Runnable action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<OrderEntity> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "select o from OrderEntity o where o.user.id = :userId",
                        OrderEntity.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}