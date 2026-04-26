package org.example.library.service.policy;

import org.example.library.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class HomeLoanBorrowPolicy implements BorrowPolicy {

    @Override
    public boolean supports(String shelfType) {
        return "HOME_LOAN".equalsIgnoreCase(shelfType);
    }

    @Override
    public void validate(BookEntity book, int days) {
        if (days > 30) {
            throw new IllegalArgumentException("Home loan books can be borrowed for max 30 days");
        }
    }
}
