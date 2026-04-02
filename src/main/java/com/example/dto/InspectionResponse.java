package com.example.dto;

import com.example.model.AssetType;
import com.example.model.InspectionRecord;
import com.example.model.InspectionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionResponse {

    private Long id;
    private Long timetableId;
    private AssetType assetType;
    private Long assetId;
    private LocalDate inspectionDate;
    private String inspectedBy;
    private InspectionStatus result;
    private String remarks;
    private LocalDateTime createdAt;

    public static InspectionResponse fromRecord(InspectionRecord r) {
        InspectionResponse res = new InspectionResponse();
        res.id = r.getId();
        res.timetableId = r.getTimetableId();
        res.assetType = r.getAssetType();
        res.assetId = r.getAssetId();
        res.inspectionDate = r.getInspectionDate();
        res.inspectedBy = r.getInspectedBy();
        res.result = r.getResult();
        res.remarks = r.getRemarks();
        res.createdAt = r.getCreatedAt();
        return res;
    }

    // ── Getters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getTimetableId() { return timetableId; }
    public AssetType getAssetType() { return assetType; }
    public Long getAssetId() { return assetId; }
    public LocalDate getInspectionDate() { return inspectionDate; }
    public String getInspectedBy() { return inspectedBy; }
    public InspectionStatus getResult() { return result; }
    public String getRemarks() { return remarks; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
