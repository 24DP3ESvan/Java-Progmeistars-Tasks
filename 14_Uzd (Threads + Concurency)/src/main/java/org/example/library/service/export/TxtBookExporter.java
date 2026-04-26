package org.example.library.service.export;

import org.example.library.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TxtBookExporter implements BookExporter {

    @Override
    public boolean supports(String format) {
        return "txt".equalsIgnoreCase(format);
    }

    @Override
    public String export(List<BookDto> books) {
        StringBuilder sb = new StringBuilder();
        for (BookDto book : books) {
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
}
