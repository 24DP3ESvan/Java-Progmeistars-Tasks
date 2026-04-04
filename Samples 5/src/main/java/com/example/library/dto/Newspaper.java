package com.example.library.dto;

public class Newspaper extends ReadingRoomBook {

    public Newspaper(String title, String author) {
        super(title, author);
    }

    @Override
    public BookType getType() {
        return BookType.NEWSPAPER;
    }
}
