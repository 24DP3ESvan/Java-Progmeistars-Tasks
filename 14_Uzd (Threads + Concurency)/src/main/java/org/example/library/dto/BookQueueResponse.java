package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookQueueResponse {
    private Long bookId;
    private int size;
    private List<String> readers;
}
