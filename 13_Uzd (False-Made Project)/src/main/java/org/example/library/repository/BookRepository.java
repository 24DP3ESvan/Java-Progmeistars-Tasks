package org.example.library.repository;

import org.example.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByBorrowed(boolean borrowed);
    List<BookEntity> findByShelfTypeIgnoreCase(String shelfType);

    int count();

    List<BookEntity> findAll();

    Optional<BookEntity> findById(Long id);

    BookEntity save(BookEntity entity);
}
