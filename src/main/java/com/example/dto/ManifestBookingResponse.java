package com.example.dto;

import com.example.enums.ManifestBookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ManifestBookingResponse {

    public ManifestBookingResponse() {
		// TODO Auto-generated constructor stub
	}
	private Long id;
    private Long manifestId;
    private Long bookingId;
    private String origin;
    private String destination;
    private String cargoType;
    private ManifestBookingStatus status;

    
}