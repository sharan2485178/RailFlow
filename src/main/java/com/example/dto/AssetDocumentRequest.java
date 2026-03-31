package com.example.dto;

import com.example.model.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetDocumentRequest {
    private AssetType assetType;
    @NotBlank private String docType;
    @NotBlank private String fileUri;
    @NotBlank private String sha256Hash;
}
