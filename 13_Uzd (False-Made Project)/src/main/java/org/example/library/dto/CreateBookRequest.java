package org.example.library.dto;

import lombok.Data;

@Data
public class CreateBookRequest {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String shelfType;
}
