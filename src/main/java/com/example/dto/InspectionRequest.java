package com.example.dto;

import com.example.model.AssetType;
import com.example.model.InspectionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InspectionRequest {

    private Long timetableId;

    @NotNull
    private AssetType assetType;

    @NotNull
    private Long assetId;

    @NotNull
    private LocalDate inspectionDate;

    @NotBlank
    private String inspectedBy;

    @NotNull
    private InspectionStatus result;

    private String remarks;

    // ── Getters & Setters ────────────────────────────────────────

    public Long getTimetableId() { return timetableId; }
    public void setTimetableId(Long timetableId) { this.timetableId = timetableId; }

    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getInspectedBy() { return inspectedBy; }
    public void setInspectedBy(String inspectedBy) { this.inspectedBy = inspectedBy; }

    public InspectionStatus getResult() { return result; }
    public void setResult(InspectionStatus result) { this.result = result; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
