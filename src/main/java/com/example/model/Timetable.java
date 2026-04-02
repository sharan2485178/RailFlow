package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long trainId;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimetableStatus status = TimetableStatus.SCHEDULED;

    // Comma-separated wagon IDs (e.g. "1,2,3")
    @Column(columnDefinition = "TEXT")
    private String wagonIdsJson;

    // Comma-separated locomotive IDs (e.g. "1,2")
    @Column(columnDefinition = "TEXT")
    private String locomotiveIdsJson;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Timetable() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getWagonIdsJson() { return wagonIdsJson; }
    public void setWagonIdsJson(String wagonIdsJson) { this.wagonIdsJson = wagonIdsJson; }

    public String getLocomotiveIdsJson() { return locomotiveIdsJson; }
    public void setLocomotiveIdsJson(String locomotiveIdsJson) { this.locomotiveIdsJson = locomotiveIdsJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Timetable{id=" + id + ", trainId=" + trainId + ", origin='" + origin
                + "', destination='" + destination + "', status=" + status + "}";
    }
}
