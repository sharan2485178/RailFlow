package com.example.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class PathConflictResponse {

    private Long id;
    private String resolutionStatus;
    private String resolutionNote;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private LocalDateTime detectedAt;

    private Long timetableId1;
    private String pathCode1;
    private LocalDateTime departureTime1;

    private Long timetableId2;
    private String pathCode2;
    private LocalDateTime departureTime2;

    
}