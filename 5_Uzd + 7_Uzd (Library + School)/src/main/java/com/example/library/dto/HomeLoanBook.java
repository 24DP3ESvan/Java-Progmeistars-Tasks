package com.example.library.dto;

import lombok.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@ToString
public class HomeLoanBook implements Book {

    private static final AtomicLong ID_SEQ = new AtomicLong();

    private final long id;
    private final String title;
    private final String author;
    private final int maxDays;
    private final Set<String> tags = new LinkedHashSet<>();

    public HomeLoanBook(String title, String author, int maxDays) {
        if (maxDays <= 0) {
            throw new IllegalArgumentException("maxDays must be > 0");
        }
        this.id = ID_SEQ.incrementAndGet();
        this.title = title;
        this.author = author;
        this.maxDays = maxDays;
    }

    @Override
    public BookType getType() {
        return BookType.HOME_LOAN;
    }

    @Override
    public boolean canBeBorrowed() {
        return true;
    }
}