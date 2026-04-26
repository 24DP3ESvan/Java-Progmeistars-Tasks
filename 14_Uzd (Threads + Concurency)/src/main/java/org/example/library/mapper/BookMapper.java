package org.example.library.mapper;

import org.example.library.dto.BookDto;
import org.example.library.dto.CreateBookRequest;
import org.example.library.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BookMapper {

    public BookDto toDto(BookEntity entity) {
        return BookDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .category(entity.getCategory())
                .shelfType(entity.getShelfType())
                .borrowed(entity.isBorrowed())
                .borrowedBy(entity.getBorrowedBy())
                .borrowDays(entity.getBorrowDays())
                .build();
    }

    public BookEntity toEntity(CreateBookRequest request) {
        return BookEntity.builder()
                .id(request.getId())
                .title(request.getTitle().trim())
                .author(request.getAuthor().trim())
                .category(request.getCategory() == null || request.getCategory().isBlank()
                        ? "UNKNOWN"
                        : request.getCategory().trim())
                .shelfType(request.getShelfType().trim().toUpperCase(Locale.ROOT))
                .borrowed(false)
                .borrowedBy(null)
                .borrowDays(null)
                .build();
    }
}
