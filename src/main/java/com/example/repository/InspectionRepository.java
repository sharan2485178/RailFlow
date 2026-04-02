package com.example.repository;

import com.example.model.AssetType;
import com.example.model.InspectionRecord;
import com.example.model.InspectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionRepository extends JpaRepository<InspectionRecord, Long> {
    List<InspectionRecord> findByTimetableId(Long timetableId);
    List<InspectionRecord> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<InspectionRecord> findByResult(InspectionStatus result);
}
