package com.example.repository;

import com.example.model.AssetType;
import com.example.model.MaintenanceRecord;
import com.example.model.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);
    List<MaintenanceRecord> findByTimetableId(Long timetableId);
    List<MaintenanceRecord> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<MaintenanceRecord> findByAssignedTo(String assignedTo);
}
