package com.example.repository;

import com.example.model.AssetDocument;
import com.example.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetDocumentRepository extends JpaRepository<AssetDocument, Long> {
    List<AssetDocument> findByAssetIdAndAssetType(Long assetId, AssetType assetType);
}
