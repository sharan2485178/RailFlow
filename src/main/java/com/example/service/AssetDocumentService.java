package com.example.service;

import com.example.dto.AssetDocumentRequest;
import com.example.model.AssetDocument;
import com.example.model.AssetType;
import com.example.model.DocumentStatus;
import com.example.repository.AssetDocumentRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetDocumentService {

    @Autowired private AssetDocumentRepository documentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditService auditService;

    public AssetDocument upload(Long assetId, AssetDocumentRequest req, String performedBy) {
        AssetDocument doc = new AssetDocument();
        doc.setAssetId(assetId);
        doc.setAssetType(req.getAssetType());
        doc.setDocType(req.getDocType());
        doc.setFileUri(req.getFileUri());
        doc.setSha256Hash(req.getSha256Hash());
        doc.setUploadedAt(LocalDateTime.now());
        doc.setStatus(DocumentStatus.ACTIVE);
        documentRepository.save(doc);

        auditService.log("UPLOAD_DOCUMENT", "AssetDocument", doc.getId().toString(),
            performedBy, "Document uploaded for " + req.getAssetType() + " id: " + assetId);
        return doc;
    }

    public List<AssetDocument> getByAsset(Long assetId, String assetType) {
        return documentRepository.findByAssetIdAndAssetType(
            assetId, AssetType.valueOf(assetType.toUpperCase()));
    }

    public AssetDocument verify(Long docId, String performedBy) {
        AssetDocument doc = documentRepository.findById(docId)
            .orElseThrow(() -> new RuntimeException("Document not found: " + docId));

        if (doc.getStatus() == DocumentStatus.REVOKED)
            throw new RuntimeException("Cannot verify a revoked document.");

        Long userId = userRepository.findByEmail(performedBy)
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();

        doc.setVerifiedBy(userId);
        documentRepository.save(doc);

        auditService.log("VERIFY_DOCUMENT", "AssetDocument", docId.toString(),
            performedBy, "Document verified by user id: " + userId);
        return doc;
    }

}
