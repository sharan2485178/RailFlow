package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetType;
import com.example.enums.YardSlotStatus;
import com.example.model.YardSlot;
@Repository
public interface YardSlotRepository extends JpaRepository<YardSlot, Long> {
    
    List<YardSlot> findByStatus(YardSlotStatus status);
    List<YardSlot> findByYardId(Long yardId);
 // available slots in a yard
    List<YardSlot> findByYardIdAndStatus(Long yardId, YardSlotStatus status);
    
    boolean existsByYardIdAndTrackNumberAndPosition(
            Long yardId, String trackNumber, Integer position);
    
    Optional<YardSlot>findByAssignedAssetTypeAndAssignedAssetId(AssetType assignedAssetType,Long assignedAssetId);
}
