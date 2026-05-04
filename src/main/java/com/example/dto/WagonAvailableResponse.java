package com.example.dto;

import com.example.enums.WagonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WagonAvailableResponse {
	private Long wagonId;
	private String serialNumber;
	private WagonType wagonType;
	private String currentYard;
	private Long currentSoltId;
	
	

}
