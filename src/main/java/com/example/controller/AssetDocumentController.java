package com.example.controller;

import com.example.dto.AssetDocumentRequest;
import com.example.model.AssetDocument;
import com.example.model.AssetType;
import com.example.service.AssetDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetDocumentController {

    @Autowired private AssetDocumentService assetDocumentService;

    @PostMapping("/{assetId}/documents/{assetType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetDocument> upload(@PathVariable Long assetId,
                                                @PathVariable String assetType,
                                                @Valid @RequestBody AssetDocumentRequest req,
                                                Authentication auth) {
        req.setAssetType(AssetType.valueOf(assetType.toUpperCase()));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(assetDocumentService.upload(assetId, req, auth.getName()));
    }

    @GetMapping("/{assetId}/documents/{assetType}")
    public ResponseEntity<List<AssetDocument>> getByAsset(@PathVariable Long assetId,
                                                           @PathVariable String assetType) {
        return ResponseEntity.ok(assetDocumentService.getByAsset(assetId, assetType));
    }

    @PutMapping("/documents/{docId}/verify")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR')")
    public ResponseEntity<AssetDocument> verify(@PathVariable Long docId,
                                                Authentication auth) {
        return ResponseEntity.ok(assetDocumentService.verify(docId, auth.getName()));
    }
}
