package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.Booking;
import com.example.model.User;
import com.example.repository.UserRepository;

@Component
public class BookingMapper {
	
	private final UserRepository userRepository;

    public BookingMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public Booking toEntity(BookingRequest req,String email) {
		
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", 101L));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setOrigin(req.getOrigin());
        booking.setDestination(req.getDestination());
        booking.setCargoType(req.getCargoType());
        booking.setWeightTon(req.getWeightTon());
        booking.setStatus(BookingStatus.PENDING);
        return booking;
	}

	public BookingResponse toDto(Booking booking) {
		BookingResponse res = new BookingResponse();
        res.setId(booking.getId());
        res.setOrigin(booking.getOrigin());
        res.setDestination(booking.getDestination());
        res.setCargoType(booking.getCargoType().name());
        res.setWeightTon(booking.getWeightTon());
        res.setStatus(booking.getStatus().name());
        res.setCreatedAt(booking.getCreatedAt());
        res.setUserId(booking.getUser().getId());
        res.setUserName(booking.getUser().getName());
        return res;
	}
	// update existing entity from request
	// userId not needed — user cannot change on update
	public Booking toUpdate(Booking existing, BookingRequest req) {
	    existing.setOrigin(req.getOrigin());
	    existing.setDestination(req.getDestination());
	    existing.setCargoType(req.getCargoType());
	    existing.setWeightTon(req.getWeightTon());
	    return existing;
	}
	
}
