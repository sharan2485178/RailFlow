package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.AssetOperationalStatus;
import com.example.enums.WagonType;
import com.example.model.Wagon;
@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {
    boolean existsBySerialNumber(String serialNumber);
    List<Wagon> findByStatus(AssetOperationalStatus status);
    List<Wagon> findByType(WagonType type);
    List<Wagon> findByTypeAndStatus(WagonType type, AssetOperationalStatus status);
}
