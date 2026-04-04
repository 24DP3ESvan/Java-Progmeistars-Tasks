package com.example.library.dto;

public class Magazine extends ReadingRoomBook {

    public Magazine(String title, String author) {
        super(title, author);
    }

    @Override
    public BookType getType() {
        return BookType.MAGAZINE;
    }
}
