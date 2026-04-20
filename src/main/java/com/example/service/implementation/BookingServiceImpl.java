package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookingMapper;
import com.example.model.Booking;
import com.example.repository.BookingRepository;
import com.example.repository.UserRepository;
import com.example.security.AuditService;
import com.example.service.BookingService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuditService auditService;

	@Autowired
	private BookingMapper bookingMapper;

	@Transactional
	public BookingResponse create(BookingRequest req, String performedBy) {

		Booking booking = bookingMapper.toEntity(req, performedBy);

		bookingRepository.save(booking);

		auditService.log("CREATE_BOOKING", "Booking", booking.getId().toString(), performedBy,
				"Booking created: " + booking.getOrigin() + " → " + booking.getDestination());

		return bookingMapper.toDto(booking);
	}
	@Transactional
	public List<BookingResponse> getByUserId(Long id) {
		return bookingRepository.findByUserId(id).stream().map(bookingMapper::toDto).collect(Collectors.toList());
	}
	@Transactional
	public List<BookingResponse> getConfirmedBooking() {
		return bookingRepository.findByStatus(BookingStatus.CONFIRMED).stream().map(bookingMapper::toDto).collect(Collectors.toList());
	}
	@Transactional
	public BookingResponse getById(Long id) {
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking", id));

		BookingResponse bookingResponse = bookingMapper.toDto(booking);
		return bookingResponse;

	}

	@Transactional
	public BookingResponse update(Long id, BookingRequest req, String perfomedBy) {
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking", id));

		if (booking.getStatus() != BookingStatus.PENDING) {
			throw new IllegalStateException(
					"Only PENDING bookings can be updated. " + "Current status: " + booking.getStatus());
		}
		bookingMapper.toUpdate(booking, req);
		bookingRepository.save(booking);
        
		
		return bookingMapper.toDto(booking);
	}

	@Transactional
	public BookingResponse changeStatus(Long id, BookingStatus newStatus, String performedBy) {
		Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking", id));

		BookingStatus currentStatus = booking.getStatus();

		// validate transition
		validateStatusTransition(currentStatus, newStatus);

		booking.setStatus(newStatus);
		bookingRepository.save(booking);

		return bookingMapper.toDto(booking);

	}


	private void validateStatusTransition(BookingStatus current, BookingStatus next) {

		boolean allowed = switch (current) {
		case PENDING -> next == BookingStatus.CONFIRMED || next == BookingStatus.CANCELLED;
		case CONFIRMED -> next == BookingStatus.ASSIGNED || next == BookingStatus.CANCELLED;
		case ASSIGNED -> next == BookingStatus.IN_TRANSIT || next == BookingStatus.CANCELLED;
		case IN_TRANSIT -> next == BookingStatus.COMPLETED;
		case COMPLETED, CANCELLED -> false;
		};

		if (!allowed) {
			throw new IllegalStateException("Invalid status transition from " + current + " to " + next);
		}
	}

}
