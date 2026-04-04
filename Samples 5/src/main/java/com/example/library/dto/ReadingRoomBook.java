package com.example.library.dto;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ReadingRoomBook implements Book, Exportable {

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

    public long getId() { return id; }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public Set<String> getTags() { return tags; }

    public BookType getType() { return BookType.READING_ROOM; }

    public String toCsvLine() {

        return getType() + ";" + id + ";" + title + ";" + author + ";" +
                tagsAsString() + ";;false";
    }

    public String getCsvHeader() {

        return "type;id;title;author;tags;maxDays;borrowed";
    }
}
