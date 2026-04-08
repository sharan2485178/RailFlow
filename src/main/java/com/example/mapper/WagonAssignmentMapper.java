package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.enums.AssetAssignmentStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.Booking;
import com.example.model.Timetable;
import com.example.model.Wagon;
import com.example.model.WagonAssignment;
import com.example.repository.BookingRepository;
import com.example.repository.TimetableRepository;
import com.example.repository.WagonRepository;

@Component
public class WagonAssignmentMapper {

    private final WagonRepository wagonRepository;
    private final TimetableRepository timetableRepository;
    private final BookingRepository bookingRepository;

    public WagonAssignmentMapper(WagonRepository wagonRepository,
                                  TimetableRepository timetableRepository,
                                  BookingRepository bookingRepository) {
        this.wagonRepository = wagonRepository;
        this.timetableRepository = timetableRepository;
        this.bookingRepository = bookingRepository;
    }

    // request → entity
    public WagonAssignment toEntity(WagonAssignmentRequest req) {

        Timetable timetable = timetableRepository.findById(req.getTimetableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Timetable", req.getTimetableId()));

        Wagon wagon = wagonRepository.findById(req.getWagonId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Wagon", req.getWagonId()));

        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Booking", req.getBookingId()));

        // cargo type must match
        if (!wagon.getType().getAllowedCargoTypes().contains(booking.getCargoType())) {
            throw new IllegalStateException(
                    "Wagon does not support cargo type "
                    + booking.getCargoType()
                    + ". Allowed: " + wagon.getType().getAllowedCargoTypes());
        }

        // wagon capacity must not be less than booking weight
        if (wagon.getCapacityTon().compareTo(booking.getWeightTon()) < 0) {
            throw new IllegalStateException(
                    "Wagon capacity " + wagon.getCapacityTon()
                    + " is less than booking weight "
                    + booking.getWeightTon());
        }

        WagonAssignment assignment = new WagonAssignment();
        assignment.setTimetable(timetable);
        assignment.setWagon(wagon);
        assignment.setBooking(booking);
        assignment.setAssetAssignmentStatus(AssetAssignmentStatus.PENDING);
        return assignment;
    }

    // entity → response DTO
    public WagonAssignmentResponse toDto(WagonAssignment assignment) {
        WagonAssignmentResponse res = new WagonAssignmentResponse();
        res.setId(assignment.getId());
        res.setTimetableId(assignment.getTimetable().getId());
        res.setBookingId(assignment.getBooking().getId());
        res.setWagonId(assignment.getWagon().getId());
        res.setWagonSerialNumber(assignment.getWagon().getSerialNumber());
        res.setWagonType(assignment.getWagon().getType().name());
        res.setAssetAssignmentStatus(assignment.getAssetAssignmentStatus());
        return res;
    }
}