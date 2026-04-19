package org.example.library.legacy;

// Специально плохой пример наследования для обсуждения Composition over Inheritance
public class ReadingRoomBook extends LibraryBook {

    @Override
    public String borrowHome() {
        throw new UnsupportedOperationException("Reading room book cannot be borrowed home");
    }
}
