package com.example.dto;

import com.example.enums.AssetOperationalStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocomotiveStatusRequest {
    @NotNull private AssetOperationalStatus status;

	public LocomotiveStatusRequest(@NotNull AssetOperationalStatus status) {
		super();
		this.status = status;
	}

	public LocomotiveStatusRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssetOperationalStatus getStatus() {
		return status;
	}

	public void setStatus(AssetOperationalStatus status) {
		this.status = status;
	}
    
    
}
