package com.example.dto;

import com.example.enums.WagonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WagonDTO {
    private Long id;
    private String wagonNumber;
    private WagonType type;
    private Long yardId;
    private Long slotId;
    private String trackNumber;
    private int position;
   
}