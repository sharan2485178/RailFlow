package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.AddBookingToManifestRequest;
import com.example.dto.ManifestBookingResponse;
import com.example.dto.ManifestRequest;
import com.example.dto.ManifestResponse;
import com.example.enums.ManifestBookingStatus;
import com.example.enums.ManifestStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.Booking;
import com.example.model.Manifest;
import com.example.model.ManifestBooking;
import com.example.repository.BookingRepository;
import com.example.repository.ManifestRepository;
import com.example.repository.TimetableRepository;

@Component
public class ManifestMapper {

    

    
    private final ManifestRepository manifestRepository;
    private final BookingRepository bookingRepository;
    private final TimetableRepository timetableRepository;

    public ManifestMapper(TimetableRepository timetableRepository, ManifestRepository manifestRepository,
                          BookingRepository bookingRepository) {
    	    this.timetableRepository = timetableRepository;
        this.manifestRepository = manifestRepository;
        this.bookingRepository = bookingRepository;
    }

    // request → entity
    public Manifest toEntity(ManifestRequest req, String createdBy) {
        Manifest manifest = new Manifest();
        manifest.setTimetable(
                timetableRepository.findById(req.getTimetableId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Timetable", req.getTimetableId())));
        manifest.setCreatedBy(createdBy);
        manifest.setStatus(ManifestStatus.DRAFT);
        return manifest;
    }
    public ManifestBooking toEntity1(Long manifestId, AddBookingToManifestRequest req) {
        Manifest manifest = manifestRepository.findById(manifestId)
                .orElseThrow(() -> new EntityNotFoundException("Manifest", manifestId));

        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking", req.getBookingId()));

        ManifestBooking mb = new ManifestBooking();
        mb.setManifest(manifest);
        mb.setBooking(booking);
        mb.setManifestBookingStatus(ManifestBookingStatus.PENDING);
        return mb;
    }

    // manifest entity → response DTO
    public ManifestResponse toDto(Manifest manifest) {
        ManifestResponse res = new ManifestResponse();
        res.setId(manifest.getId());
        res.setTimetableId(manifest.getTimetable().getId());
        res.setPathCode(manifest.getTimetable().getPathCode());
        res.setDepartureTime(manifest.getTimetable().getDepartureTime());
        res.setCreatedBy(manifest.getCreatedBy());
        res.setCreatedAt(manifest.getCreatedAt());
        res.setStatus(manifest.getStatus());
        return res;
    }

    // manifest booking entity → response DTO
    public ManifestBookingResponse toBookingDto(ManifestBooking mb) {
        ManifestBookingResponse res = new ManifestBookingResponse();
        res.setId(mb.getId());
        res.setManifestId(mb.getManifest().getId());
        res.setBookingId(mb.getBooking().getId());
        res.setOrigin(mb.getBooking().getOrigin());
        res.setDestination(mb.getBooking().getDestination());
        res.setCargoType(mb.getBooking().getCargoType().name());
        res.setStatus(mb.getManifestBookingStatus());
        return res;
    }
   
}