package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetType;
import com.example.enums.MaintenanceStatus;
import com.example.model.MaintenanceRecord;
@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);
    List<MaintenanceRecord> findByTimetableId(Long timetableId);
    List<MaintenanceRecord> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<MaintenanceRecord> findByAssignedTo(String assignedTo);
}
