package com.example.service;

import java.util.List;

import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.enums.BookingStatus;

public interface BookingService {
    BookingResponse create(BookingRequest req, String performedBy);
    List<BookingResponse> getByUserId(Long id);
    List<BookingResponse> getConfirmedBooking();
    BookingResponse getById(Long id);
    BookingResponse update(Long id, BookingRequest req, String perfomedBy);
    BookingResponse changeStatus(Long id, BookingStatus newStatus, String performedBy);
}
