package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String shelfType;
    private boolean borrowed;
    private String borrowedBy;
    private Integer borrowDays;
}
