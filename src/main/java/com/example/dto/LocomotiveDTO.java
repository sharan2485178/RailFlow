package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocomotiveDTO {
    private Long id;
    private String locomotiveNumber;
    private String model;
}