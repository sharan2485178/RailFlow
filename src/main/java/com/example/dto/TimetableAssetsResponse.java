package com.example.dto;

import com.example.model.Locomotive;
import com.example.model.TimetableStatus;
import com.example.model.Wagon;
import java.time.LocalDateTime;
import java.util.List;

public class TimetableAssetsResponse {

    private Long timetableId;
    private Long trainId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private TimetableStatus status;
    private List<Wagon> wagons;
    private List<Locomotive> locomotives;

    public TimetableAssetsResponse() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getTimetableId() { return timetableId; }
    public void setTimetableId(Long timetableId) { this.timetableId = timetableId; }

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

    public TimetableStatus getStatus() { return status; }
    public void setStatus(TimetableStatus status) { this.status = status; }

    public List<Wagon> getWagons() { return wagons; }
    public void setWagons(List<Wagon> wagons) { this.wagons = wagons; }

    public List<Locomotive> getLocomotives() { return locomotives; }
    public void setLocomotives(List<Locomotive> locomotives) { this.locomotives = locomotives; }
}
