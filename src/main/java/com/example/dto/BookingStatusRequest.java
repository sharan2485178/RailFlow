package com.example.dto;

import com.example.model.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingStatusRequest {
    @NotNull private BookingStatus status;
}
