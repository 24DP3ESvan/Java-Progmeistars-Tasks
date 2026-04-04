package com.example.library.service;

import com.example.library.dto.*;

import java.io.*;
import java.util.*;

public class LibraryService {

    private final Map<BookType, List<Book>> booksByType =
            new EnumMap<>(BookType.class);

    public LibraryService() {

        for (BookType t : BookType.values()) {

            booksByType.put(t, new ArrayList<>());
        }
    }

    public void add(Book book) {

        booksByType.get(book.getType()).add(book);
    }

    public boolean borrow(Book book) {

        if (book instanceof Borrowable b) {

            if (b.canBeBorrowed()) {

                b.borrow();
                return true;
            }
        }

        return false;
    }

    public void returnBook(Book book) {

        if (book instanceof Borrowable b) {

            b.returnBook();
        }
    }

    public void saveByType(File folder) throws IOException {

        for (BookType type : BookType.values()) {

            File file = new File(folder, type.name() + ".txt");

            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

                out.println("type;id;title;author;tags;maxDays;borrowed");

                for (Book b : booksByType.get(type)) {

                    if (b instanceof Exportable e) {

                        out.println(e.toCsvLine());
                    }
                }
            }
        }
    }

    public void saveBorrowedBooks(File file) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            for (BookType t : BookType.values()) {

                for (Book b : booksByType.get(t)) {

                    if (b instanceof Borrowable br && br.isBorrowed()) {

                        out.println(b.getTitle() + " - " + b.getAuthor());
                    }
                }
            }
        }
    }
}
