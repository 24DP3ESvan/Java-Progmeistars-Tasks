package org.example.library.service.export;

import org.example.library.dto.BookDto;

import java.util.List;

public interface BookExporter {
    boolean supports(String format);
    String export(List<BookDto> books);
}
