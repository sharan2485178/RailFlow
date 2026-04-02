package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wagon")
public class Wagon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WagonType type;

    @Column(name = "capacity_ton", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacityTon;

    @Column(name = "serial_number", length = 50, unique = true, nullable = false)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetOperationalStatus status = AssetOperationalStatus.AVAILABLE;

    // ── Constructors ─────────────────────────────────────────────

    public Wagon() {}

    // ── Getters & Setters ────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WagonType getType() { return type; }
    public void setType(WagonType type) { this.type = type; }

    public BigDecimal getCapacityTon() { return capacityTon; }
    public void setCapacityTon(BigDecimal capacityTon) { this.capacityTon = capacityTon; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public AssetOperationalStatus getStatus() { return status; }
    public void setStatus(AssetOperationalStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Wagon{id=" + id + ", serialNumber='" + serialNumber + "', type=" + type + ", status=" + status + "}";
    }
}
