package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import com.example.enums.AssetOperationalStatus;

@Entity
@Table(name = "locomotive")
public class Locomotive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(name = "capacity_ton", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacityTon;

    @Column(name = "serial_number", length = 50, unique = true, nullable = false)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetOperationalStatus status = AssetOperationalStatus.AVAILABLE;

    // ── Constructors ─────────────────────────────────────────────

    public Locomotive() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public BigDecimal getCapacityTon() { return capacityTon; }
    public void setCapacityTon(BigDecimal capacityTon) { this.capacityTon = capacityTon; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public AssetOperationalStatus getStatus() { return status; }
    public void setStatus(AssetOperationalStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Locomotive{id=" + id + ", serialNumber='" + serialNumber + "', model='" + model + "', status=" + status + "}";
    }
}
