package com.example.repository;

import com.example.model.Manifest;
import com.example.model.ManifestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManifestRepository extends JpaRepository<Manifest, Long> {
    List<Manifest> findByStatus(ManifestStatus status);
    List<Manifest> findByTrainId(Long trainId);
}
