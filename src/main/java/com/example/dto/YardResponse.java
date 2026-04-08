package com.example.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YardResponse {
    private Long id;
    private String name;
    private String location;
    private Integer totalSlots;
    private LocalDateTime createdAt;
}