package com.example.repository;

import com.example.model.AssetType;
import com.example.model.ShuntingOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShuntingOperationRepository extends JpaRepository<ShuntingOperation, Long> {
    List<ShuntingOperation> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<ShuntingOperation> findByFromSlotIdOrToSlotId(Long fromSlotId, Long toSlotId);
}
