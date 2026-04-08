package com.example.dto;

import com.example.enums.AssetAssignmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class LocomotiveAssignmentResponse {

    public LocomotiveAssignmentResponse() {
		// TODO Auto-generated constructor stub
	}
	private Long id;
    private Long timetableId;
    private Long locomotiveId;
    private String locomotiveModel;
    private String serialNumber;
    private AssetAssignmentStatus status;

    
}