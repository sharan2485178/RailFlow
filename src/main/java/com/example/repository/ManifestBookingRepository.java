package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.ManifestBooking;

@Repository
public interface ManifestBookingRepository
        extends JpaRepository<ManifestBooking, Long> {

    List<ManifestBooking> findByManifestId(Long manifestId);

    boolean existsByManifestIdAndBookingId(Long manifestId, Long bookingId);
}