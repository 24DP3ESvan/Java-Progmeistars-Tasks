package com.example.library.service;

import com.example.library.dto.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {

    private final Map<BookType, List<Book>> booksByType = new EnumMap<>(BookType.class);
    private final Set<String> allTags = new LinkedHashSet<>();
    private final Map<BookType, Integer> borrowStats = new EnumMap<>(BookType.class);

    public LibraryService() {
        Arrays.stream(BookType.values()).forEach(t -> {
            booksByType.put(t, new ArrayList<>());
            borrowStats.put(t, 0);
        });
    }

    public Book create(BookType type, String title, String author, Integer maxDaysOrNull) {
        return switch (type) {
            case READING_ROOM -> new ReadingRoomBook(title, author);
            case HOME_LOAN -> new HomeLoanBook(title, author, maxDaysOrNull == null ? 14 : maxDaysOrNull);
        };
    }

    public void add(Book book) {
        booksByType.get(book.getType()).add(book);
        allTags.addAll(book.getTagsReadOnly());
    }

    public boolean borrow(Book book) {
        if (!book.canBeBorrowed()) return false;

        borrowStats.put(book.getType(), borrowStats.get(book.getType()) + 1);
        return true;
    }

    public int borrowAll(Collection<Book> books) {
        return (int) books.stream()
                .filter(this::borrow)
                .count();
    }

    public List<Book> getBooksByType(BookType type) {
        return Collections.unmodifiableList(booksByType.get(type));
    }

    public Set<String> getAllTags() {
        return Collections.unmodifiableSet(allTags);
    }

    public int getBorrowCount(BookType type) {
        return borrowStats.get(type);
    }

    public String buildReport() {
        String perType = Arrays.stream(BookType.values())
                .map(t -> t + ": " + booksByType.get(t).size() + " books, borrowed=" + getBorrowCount(t))
                .collect(Collectors.joining("\n"));

        return "=== Library report ===\n" +
                perType +
                "\nAll tags: " + allTags + "\n";
    }

    /** Выгружаем библиотеку в CSV */
    public void save(File file) throws IOException {
        var allBooks = Arrays.stream(BookType.values())
                .flatMap(t -> booksByType.get(t).stream())
                .toList();

        try {
            LibraryCsvStorage.save(file, allBooks);
        } catch (IOException e) {
            throw new IOException("Failed to save library to file: " + file.getAbsolutePath(), e);
        }
    }

    /** Загружаем библиотеку из CSV */
    public void load(File file) throws IOException {
        Arrays.stream(BookType.values()).forEach(t -> {
            booksByType.get(t).clear();
            borrowStats.put(t, 0);
        });
        allTags.clear();

        try {
            LibraryCsvStorage.load(file, this::add);
        } catch (IOException e) {
            throw new IOException("Failed to load library from file: " + file.getAbsolutePath(), e);
        } catch (RuntimeException e) {
            throw new IOException("Invalid CSV content in " + file.getAbsolutePath(), e);
        }
    }
}