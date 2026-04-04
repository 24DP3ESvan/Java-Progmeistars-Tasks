package com.example.library;

import com.example.library.dto.*;
import com.example.library.service.LibraryService;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        LibraryService lib = new LibraryService();

        Book b1 = new ReadingRoomBook("History of Latvia", "Teacher");

        Book b2 = new HomeLoanBook("Algebra", "Math Teacher", 21);

        Book b3 = new ChildrenBook("Harry Potter", "Rowling");

        Book b4 = new PhotoAlbum("Nature Photos", "Photographer");

        Book b5 = new Magazine("Science Monthly", "Editorial");

        Book b6 = new Newspaper("Daily News", "Editorial");

        lib.add(b1);
        lib.add(b2);
        lib.add(b3);
        lib.add(b4);
        lib.add(b5);
        lib.add(b6);

        lib.borrow(b2);
        lib.borrow(b3);

        File folder = new File("library_files");
        folder.mkdir();

        lib.saveByType(folder);

        lib.saveBorrowedBooks(new File("borrowed_books.txt"));

        System.out.println("Files exported.");
    }
}
