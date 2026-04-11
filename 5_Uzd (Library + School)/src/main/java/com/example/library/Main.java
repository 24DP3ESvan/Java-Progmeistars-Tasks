package com.example.library;

import com.example.library.dto.*;
import com.example.library.service.LibraryService;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // try / catch / finally (error handling intro)
        try {
            LibraryService lib = new LibraryService();

            Book b1 = lib.create(BookType.READING_ROOM, "History of Latvia", "A. Teacher", null);
            // lambda + method reference
            List.of("history", "Latvia", "history")
                    .forEach(b1::addTag);

            Book b2 = lib.create(BookType.HOME_LOAN, "Algebra 10", "B. Teacher", 21);
            List.of("math", "school").forEach(b2::addTag);

            Book b3 = new HomeLoanBook("Harry Potter", "J.K. Rowling", 30);
            List.of("fiction", "fantasy").forEach(b3::addTag);

            List.of(b1, b2, b3).forEach(lib::add);

            System.out.println("Borrow b1 (reading room): " + lib.borrow(b1)); // false
            System.out.println("Borrow b2 (home loan): " + lib.borrow(b2));    // true

            int ok = lib.borrowAll(List.of(b1, b2, b3));
            System.out.println("BorrowAll success count: " + ok);

            System.out.println();
            System.out.println(lib.buildReport());

            File file = new File("library.csv");
            lib.save(file);
            System.out.println("Saved to: " + file.getAbsolutePath());

            LibraryService loaded = new LibraryService();
            loaded.load(file);
            System.out.println("\nLoaded from file:");
            System.out.println(loaded.buildReport());

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("\n[done]");
        }
    }
}