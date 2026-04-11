package com.example.library.dto;

import lombok.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@ToString
public class ReadingRoomBook implements Book {

    private static final AtomicLong ID_SEQ = new AtomicLong();

    private final long id;
    private final String title;
    private final String author;
    private final Set<String> tags = new LinkedHashSet<>();

    public ReadingRoomBook(String title, String author) {
        this.id = ID_SEQ.incrementAndGet();
        this.title = title;
        this.author = author;
    }

    @Override
    public BookType getType() {
        return BookType.READING_ROOM;
    }

    @Override
    public boolean canBeBorrowed() {
        return false;
    }
}