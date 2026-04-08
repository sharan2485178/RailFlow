package com.example.dto;

import com.example.enums.TrainStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class TrainResponse {

    private Long id;
    private String number;
    private String operator;
    private String origin;
    private String destination;
    private TrainStatus status;

    

    public TrainResponse(Long id, String number, String operator,
                         String origin, String destination, TrainStatus status) {
        this.id = id;
        this.number = number;
        this.operator = operator;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
    }

    
}