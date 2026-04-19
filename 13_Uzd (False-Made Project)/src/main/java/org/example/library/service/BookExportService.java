package org.example.library.service;

import org.example.library.entity.BookEntity;

import java.util.List;

public class BookExportService {

    public String export(List<BookEntity> books, String format) {
        if (format == null || format.isBlank()) {
            format = "txt";
        }

        if ("txt".equalsIgnoreCase(format)) {
            StringBuilder sb = new StringBuilder();
            for (BookEntity book : books) {
                sb.append(book.getId())
                        .append(" | ")
                        .append(book.getTitle())
                        .append(" | ")
                        .append(book.getAuthor())
                        .append(" | ")
                        .append(book.getShelfType())
                        .append(" | borrowed=")
                        .append(book.isBorrowed())
                        .append("\n");
            }
            return sb.toString();
        }

        if ("csv".equalsIgnoreCase(format)) {
            StringBuilder sb = new StringBuilder();
            sb.append("id,title,author,category,shelfType,borrowed,borrowedBy,borrowDays\n");
            for (BookEntity book : books) {
                sb.append(book.getId()).append(',')
                        .append(book.getTitle()).append(',')
                        .append(book.getAuthor()).append(',')
                        .append(book.getCategory()).append(',')
                        .append(book.getShelfType()).append(',')
                        .append(book.isBorrowed()).append(',')
                        .append(book.getBorrowedBy() == null ? "" : book.getBorrowedBy()).append(',')
                        .append(book.getBorrowDays() == null ? "" : book.getBorrowDays())
                        .append('\n');
            }
            return sb.toString();
        }

        throw new IllegalArgumentException("Unsupported format: " + format);
    }
}
