package org.example.library.service.export;

import org.example.library.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvBookExporter implements BookExporter {

    @Override
    public boolean supports(String format) {
        return "csv".equalsIgnoreCase(format);
    }

    @Override
    public String export(List<BookDto> books) {
        StringBuilder sb = new StringBuilder("id,title,author,category,shelfType,borrowed,borrowedBy,borrowDays\n");
        for (BookDto book : books) {
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
}
