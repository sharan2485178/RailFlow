package com.example.repository;

import com.example.model.AssetOperationalStatus;
import com.example.model.Locomotive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocomotiveRepository extends JpaRepository<Locomotive, Long> {
    boolean existsBySerialNumber(String serialNumber);
    List<Locomotive> findByStatus(AssetOperationalStatus status);
}
