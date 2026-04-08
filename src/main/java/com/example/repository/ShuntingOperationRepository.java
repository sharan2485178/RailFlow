package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetType;
import com.example.model.ShuntingOperation;
@Repository
public interface ShuntingOperationRepository extends JpaRepository<ShuntingOperation, Long> {
    List<ShuntingOperation> findByAssetTypeAndAssetId(AssetType assetType, Long assetId);
    List<ShuntingOperation> findByFromSlotIdOrToSlotId(Long fromSlotId, Long toSlotId);
}
