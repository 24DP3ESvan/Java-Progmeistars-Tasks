package org.example.repository;

import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.age >= :minAge and u.status = :status order by u.id")
    List<UserEntity> findByMinAgeAndStatus(@Param("minAge") int minAge, @Param("status") int status);

    @Query("select avg(u.age) from UserEntity u")
    Double findAverageAge();

    UserEntity save(UserEntity entity);

    Collection<Object> findAll();
}
