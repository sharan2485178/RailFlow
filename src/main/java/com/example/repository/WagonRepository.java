package com.example.repository;

import com.example.model.AssetOperationalStatus;
import com.example.model.Wagon;
import com.example.model.WagonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WagonRepository extends JpaRepository<Wagon, Long> {
    boolean existsBySerialNumber(String serialNumber);
    List<Wagon> findByStatus(AssetOperationalStatus status);
    List<Wagon> findByType(WagonType type);
    List<Wagon> findByTypeAndStatus(WagonType type, AssetOperationalStatus status);
}
