package com.example.enums;

public enum MaintenanceStatus {
    OPEN, // reported, not yet assigned
    ASSIGNED, // assigned to maintenance crew
    IN_PROGRESS, // work started
    COMPLETED, // work done, asset ready
    CANCELLED // cancelled — not needed
}
