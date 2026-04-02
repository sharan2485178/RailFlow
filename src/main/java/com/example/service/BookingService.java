package com.example.service;

import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.dto.BookingStatusRequest;
import com.example.model.Booking;
import com.example.model.BookingStatus;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditService auditService;

    public BookingResponse create(BookingRequest req, String performedBy) {
        User user = userRepository.findById(req.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setOrigin(req.getOrigin());
        booking.setDestination(req.getDestination());
        booking.setCargoDetailsJson(req.getCargoDetailsJson());
        booking.setWeightTon(req.getWeightTon());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        auditService.log("CREATE_BOOKING", "Booking", booking.getId().toString(),
            performedBy, "Booking created: " + booking.getOrigin() + " → " + booking.getDestination());
        return BookingResponse.fromBooking(booking);
    }

    public List<BookingResponse> getAll(String status) {
        List<Booking> bookings;
        if (status != null && !status.isBlank())
            bookings = bookingRepository.findByStatus(BookingStatus.valueOf(status.toUpperCase()));
        else
            bookings = bookingRepository.findAll();
        return bookings.stream().map(BookingResponse::fromBooking).collect(Collectors.toList());
    }

    public BookingResponse getById(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        return BookingResponse.fromBooking(booking);
    }

    public List<BookingResponse> getByUser(Long userId) {
        return bookingRepository.findByUserId(userId)
            .stream().map(BookingResponse::fromBooking).collect(Collectors.toList());
    }

    public BookingResponse update(Long id, BookingRequest req, String performedBy) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        User user = userRepository.findById(req.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));

        booking.setUser(user);
        booking.setOrigin(req.getOrigin());
        booking.setDestination(req.getDestination());
        booking.setCargoDetailsJson(req.getCargoDetailsJson());
        booking.setWeightTon(req.getWeightTon());
        bookingRepository.save(booking);

        auditService.log("UPDATE_BOOKING", "Booking", id.toString(),
            performedBy, "Booking updated: " + booking.getOrigin() + " → " + booking.getDestination());
        return BookingResponse.fromBooking(booking);
    }

    public BookingResponse changeStatus(Long id, BookingStatusRequest req, String performedBy) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(req.getStatus());
        bookingRepository.save(booking);

        auditService.log("CHANGE_BOOKING_STATUS", "Booking", id.toString(),
            performedBy, "Status changed: " + oldStatus + " → " + req.getStatus());
        return BookingResponse.fromBooking(booking);
    }
}
