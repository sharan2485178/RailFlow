package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class YardSlotRequest {

    @NotNull(message = "Yard ID is required")
    private Long yardId;

    @NotBlank(message = "Track number is required")
    private String trackNumber;

    @NotNull(message = "Position is required")
    private Integer position;

}