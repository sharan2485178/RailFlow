package com.example.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.enums.TimetableStatus;
import com.example.model.Locomotive;
import com.example.model.Wagon;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class TimetableAssetsResponse {

    private Long timetableId;
    private Long trainId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private TimetableStatus status;
    private List<Wagon> wagons;
    private List<Locomotive> locomotives;

    

    // ── Getters & Setters ────────────────────────────────────────

    
}
