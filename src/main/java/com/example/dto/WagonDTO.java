package com.example.dto;

import com.example.enums.WagonType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WagonDTO {
    private Long id;
    private String wagonNumber;
    private WagonType type;
    // map only the fields you need
}