package com.example.library.dto;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class HomeLoanBook implements Book, Exportable, Borrowable {

    private static final AtomicLong ID_SEQ = new AtomicLong();

    private final long id;
    private final String title;
    private final String author;
    private final int maxDays;
    private final Set<String> tags = new LinkedHashSet<>();

    private boolean borrowed = false;

    public HomeLoanBook(String title, String author, int maxDays) {

        this.id = ID_SEQ.incrementAndGet();
        this.title = title;
        this.author = author;
        this.maxDays = maxDays;
    }

    public long getId() { return id; }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public int getMaxDays() { return maxDays; }

    public Set<String> getTags() { return tags; }

    public BookType getType() { return BookType.HOME_LOAN; }

    public boolean canBeBorrowed() { return !borrowed; }

    public boolean isBorrowed() { return borrowed; }

    public void borrow() { borrowed = true; }

    public void returnBook() { borrowed = false; }

    public String toCsvLine() {

        return getType() + ";" + id + ";" + title + ";" + author + ";" +
                tagsAsString() + ";" + maxDays + ";" + borrowed;
    }

    public String getCsvHeader() {

        return "type;id;title;author;tags;maxDays;borrowed";
    }
}
