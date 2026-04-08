package com.example.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

	private Long id;
    private String origin;
    private String destination;
    private String cargoType;
    private Double weightTon;
    private String status;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;

    
}
