package org.example.library.service.validation;

import org.example.library.dto.BorrowBookRequest;
import org.example.library.dto.ExtendBorrowRequest;
import org.example.library.dto.QueueBookRequest;
import org.example.library.dto.CreateBookRequest;
import org.springframework.stereotype.Component;

@Component
public class BookRequestValidator {

    public void validateCreateRequest(CreateBookRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new IllegalArgumentException("author is required");
        }
        if (request.getShelfType() == null || request.getShelfType().isBlank()) {
            throw new IllegalArgumentException("shelfType is required");
        }
    }

    public void validateBorrowRequest(BorrowBookRequest request) {
        if (request.getReaderName() == null || request.getReaderName().isBlank()) {
            throw new IllegalArgumentException("readerName is required");
        }
        if (request.getDays() == null || request.getDays() <= 0) {
            throw new IllegalArgumentException("days must be positive");
        }
    }

    public void validateExtendRequest(ExtendBorrowRequest request) {
        if (request.getAdditionalDays() == null || request.getAdditionalDays() <= 0) {
            throw new IllegalArgumentException("additionalDays must be positive");
        }
    }

    public void validateQueueRequest(QueueBookRequest request) {
        if (request.getReaderName() == null || request.getReaderName().isBlank()) {
            throw new IllegalArgumentException("readerName is required");
        }
    }
}
