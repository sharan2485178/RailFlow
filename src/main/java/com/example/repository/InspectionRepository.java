package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetType;
import com.example.enums.InspectionStatus;
import com.example.model.InspectionRecord;
@Repository
public interface InspectionRepository extends JpaRepository<InspectionRecord, Long> {
    List<InspectionRecord> findByTimetableId(Long timetableId);
    List<InspectionRecord> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<InspectionRecord> findByResult(InspectionStatus result);
}
