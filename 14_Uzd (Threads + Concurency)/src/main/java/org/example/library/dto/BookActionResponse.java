package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookActionResponse {
    private String message;
    private BookDto book;
}
