package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private Long id;
    private String product;
    private double price;
    private Long userId;
}