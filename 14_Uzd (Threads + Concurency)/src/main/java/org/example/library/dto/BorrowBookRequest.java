package org.example.library.dto;

import lombok.Data;

@Data
public class BorrowBookRequest {
    private String readerName;
    private Integer days;
}
