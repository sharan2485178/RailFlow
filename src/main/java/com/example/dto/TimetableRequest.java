package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TimetableRequest {

    @NotNull
    private Long trainId;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    // Comma-separated IDs e.g. "1,2,3"
    private String wagonIdsJson;

    // Comma-separated IDs e.g. "1,2"
    private String locomotiveIdsJson;

    // ── Getters & Setters ────────────────────────────────────────

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getWagonIdsJson() { return wagonIdsJson; }
    public void setWagonIdsJson(String wagonIdsJson) { this.wagonIdsJson = wagonIdsJson; }

    public String getLocomotiveIdsJson() { return locomotiveIdsJson; }
    public void setLocomotiveIdsJson(String locomotiveIdsJson) { this.locomotiveIdsJson = locomotiveIdsJson; }
}
