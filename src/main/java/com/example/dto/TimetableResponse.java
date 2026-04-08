package com.example.dto;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class TimetableResponse{
    private Long id;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String pathCode;
    private String status;
    private Long trainId;

    

    
}