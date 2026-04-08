package com.example.dto;

import com.example.enums.AssetAssignmentStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class WagonAssignmentResponse {
	
	private Long id;
	private Long wagonId;
	private Long bookingId;
	private Long timetableId;
	private AssetAssignmentStatus assetAssignmentStatus;
	private String wagonSerialNumber;
    private String wagonType;
	
	
	
	

}
