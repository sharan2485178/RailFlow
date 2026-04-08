package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocomotiveRequest {
    @NotBlank private String model;
    @NotNull @Positive private BigDecimal capacityTon;
    @NotBlank private String serialNumber;
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public BigDecimal getCapacityTon() {
		return capacityTon;
	}
	public void setCapacityTon(BigDecimal capacityTon) {
		this.capacityTon = capacityTon;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public LocomotiveRequest(@NotBlank String model, @NotNull @Positive BigDecimal capacityTon,
			@NotBlank String serialNumber) {
		super();
		this.model = model;
		this.capacityTon = capacityTon;
		this.serialNumber = serialNumber;
	}
	public LocomotiveRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
