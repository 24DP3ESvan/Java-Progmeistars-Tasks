package com.example.library.dto;

public class ChildrenBook extends HomeLoanBook {

    public ChildrenBook(String title, String author) {
        super(title, author, 14);
    }

    @Override
    public BookType getType() {
        return BookType.CHILDREN_BOOK;
    }
}
