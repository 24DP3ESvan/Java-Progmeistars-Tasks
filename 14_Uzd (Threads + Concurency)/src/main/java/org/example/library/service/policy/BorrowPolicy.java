package org.example.library.service.policy;

import org.example.library.entity.BookEntity;

public interface BorrowPolicy {
    boolean supports(String shelfType);
    void validate(BookEntity book, int days);
}
