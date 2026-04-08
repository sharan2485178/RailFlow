package com.example.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.enums.AssetType;
import com.example.enums.InspectionStatus;
import com.example.model.InspectionRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    
}
