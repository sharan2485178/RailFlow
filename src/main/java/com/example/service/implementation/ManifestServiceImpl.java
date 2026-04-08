package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.AddBookingToManifestRequest;
import com.example.dto.ManifestBookingResponse;
import com.example.dto.ManifestRequest;
import com.example.dto.ManifestResponse;
import com.example.enums.BookingStatus;
import com.example.enums.ManifestStatus;
import com.example.enums.TimetableStatus;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.ManifestMapper;
import com.example.model.Booking;
import com.example.model.Manifest;
import com.example.model.ManifestBooking;
import com.example.repository.BookingRepository;
import com.example.repository.ManifestBookingRepository;
import com.example.repository.ManifestRepository;
import com.example.repository.TimetableRepository;
import com.example.service.ManifestService;

@Service
public class ManifestServiceImpl implements ManifestService {

    private final ManifestRepository manifestRepository;
    private final ManifestBookingRepository manifestBookingRepository;
    private final BookingRepository bookingRepository;
    private final TimetableRepository timetableRepository;
    private final ManifestMapper manifestMapper;

    public ManifestServiceImpl(ManifestRepository manifestRepository,
                            ManifestBookingRepository manifestBookingRepository,
                            BookingRepository bookingRepository,
                            TimetableRepository timetableRepository,
                            ManifestMapper manifestMapper) {
        this.manifestRepository = manifestRepository;
        this.manifestBookingRepository = manifestBookingRepository;
        this.bookingRepository = bookingRepository;
        this.timetableRepository = timetableRepository;
        this.manifestMapper = manifestMapper;
    }

    @Transactional
    public ManifestResponse create(ManifestRequest req, String createdBy) {

        // timetable must be PUBLISHED
        timetableRepository.findById(req.getTimetableId())
                .filter(t -> t.getStatus() == TimetableStatus.PUBLISHED)
                .orElseThrow(() -> new IllegalStateException(
                        "Timetable must be PUBLISHED to create a manifest"));

        // one manifest per timetable
        if (manifestRepository.existsByTimetableId(req.getTimetableId())) {
            throw new IllegalStateException(
                    "Manifest already exists for timetable "
                    + req.getTimetableId());
        }

        Manifest manifest = manifestMapper.toEntity(req, createdBy);
        manifestRepository.save(manifest);
        return manifestMapper.toDto(manifest);
    }

    @Transactional(readOnly = true)
    public List<ManifestResponse> getAll() {
        return manifestRepository.findAll()
                .stream()
                .map(manifestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ManifestResponse getById(Long id) {
        Manifest manifest = manifestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manifest", id));
        return manifestMapper.toDto(manifest);
    }

    @Transactional
    public ManifestBookingResponse addBooking(Long manifestId,
                                               AddBookingToManifestRequest req) {

        // manifest must be DRAFT
        Manifest manifest = manifestRepository.findById(manifestId)
                .orElseThrow(() -> new EntityNotFoundException("Manifest", manifestId));

        if (manifest.getStatus() != ManifestStatus.DRAFT) {
            throw new IllegalStateException(
                    "Cannot add bookings to manifest in status: "
                    + manifest.getStatus());
        }

        // booking must be CONFIRMED
        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking", req.getBookingId()));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Booking must be CONFIRMED before adding to manifest. "
                    + "Current status: " + booking.getStatus());
        }

        // booking must not already be in this manifest
        if (manifestBookingRepository.existsByManifestIdAndBookingId(
                manifestId, req.getBookingId())) {
            throw new IllegalStateException(
                    "Booking " + req.getBookingId() + " is already in this manifest");
        }

        // create and save manifest booking
        ManifestBooking mb = manifestMapper.toEntity1(manifestId, req);
        manifestBookingRepository.save(mb);

        return manifestMapper.toBookingDto(mb);
    }

    @Transactional(readOnly = true)
    public List<ManifestBookingResponse> getBookings(Long manifestId) {

        manifestRepository.findById(manifestId)
                .orElseThrow(() -> new EntityNotFoundException("Manifest", manifestId));

        return manifestBookingRepository.findByManifestId(manifestId)
                .stream()
                .map(manifestMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    
}
