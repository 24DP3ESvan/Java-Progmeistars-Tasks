package org.example.library.service.policy;

import org.example.library.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class ReadingRoomBorrowPolicy implements BorrowPolicy {

    @Override
    public boolean supports(String shelfType) {
        return "READING_ROOM".equalsIgnoreCase(shelfType);
    }

    @Override
    public void validate(BookEntity book, int days) {
        throw new IllegalStateException("Reading room books cannot be taken home");
    }
}
