package com.example.dto;

import lombok.Data;

@Data
public class YardSlotRequest {
    private String slotCode;
    private String yardName;
    private String trackNumber;
    private Integer capacity;
}
