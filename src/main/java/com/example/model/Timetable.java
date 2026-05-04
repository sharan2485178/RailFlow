package com.example.model;
import com.example.enums.TimetableStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.example.enums.TimetableStatus;

import jakarta.persistence.criteria.Fetch;

@Entity
@Table(name = "timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable=false)
    private String pathCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimetableStatus status = TimetableStatus.DRAFT; // DRAFT,ON_HOLD,PUBLISHED,IN_DEPARTED,COMPLETED, CANCELLED

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="train_id",nullable=false)
    private Train train;
    

    public Timetable() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public TimetableStatus getStatus() { return status; }
    public void setStatus(TimetableStatus status) { this.status = status; }

    public String getPathCode() {
        return pathCode;
    }

    public void setPathCode(String pathCode) {
        this.pathCode = pathCode;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    

    
}
