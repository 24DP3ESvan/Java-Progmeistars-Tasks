package com.example.library.dto;

public class BookRow {
    public String type;      // READING_ROOM / HOME_LOAN
    public String title;
    public String author;
    public String tags;      // tag1|tag2|tag3
    public Integer maxDays;  // null для READING_ROOM

    public BookRow() {}
}