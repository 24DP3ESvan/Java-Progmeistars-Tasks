package com.example.library.dto;

public class PhotoAlbum extends HomeLoanBook {

    public PhotoAlbum(String title, String author) {
        super(title, author, 30);
    }

    @Override
    public BookType getType() {
        return BookType.PHOTO_ALBUM;
    }
}
