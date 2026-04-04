package com.example.library.dto;

public interface Borrowable {

    boolean canBeBorrowed();

    void borrow();

    void returnBook();

    boolean isBorrowed();
}