package com.example.dto;

import java.time.LocalDateTime;

import com.example.enums.ManifestStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ManifestResponse {

    private Long id;
    private Long timetableId;
    private String pathCode;
    private LocalDateTime departureTime;
    private String createdBy;
    private LocalDateTime createdAt;
    private ManifestStatus status;

}