package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class WagonAssignmentRequest {
	
	@NotNull(message="Timetable Id is necessary")
	private Long timetableId;
	
	@NotNull(message="Booking Id is necessary")
	private Long bookingId;
	
	@NotNull(message="")
	private Long wagonId;

	
	
	
	

}
