package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "manifest")
public class Manifest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(columnDefinition = "TEXT")
    private String bookingIdsJson;

    @Column(nullable = false)
    private String createdBy;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManifestStatus status = ManifestStatus.DRAFT;

    // ── Constructors ─────────────────────────────────────────────

    public Manifest() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public String getBookingIdsJson() { return bookingIdsJson; }
    public void setBookingIdsJson(String bookingIdsJson) { this.bookingIdsJson = bookingIdsJson; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public ManifestStatus getStatus() { return status; }
    public void setStatus(ManifestStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Manifest{id=" + id + ", status=" + status + ", createdBy='" + createdBy + "'}";
    }
}
