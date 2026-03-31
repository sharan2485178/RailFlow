package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "yard_slot")
@Data
public class YardSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String slotCode;

    @Column(nullable = false)
    private String yardName;

    @Column(nullable = false)
    private String trackNumber;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YardSlotStatus status = YardSlotStatus.AVAILABLE;

    private LocalDateTime createdAt;
}
