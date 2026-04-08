package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.ManifestStatus;
import com.example.model.Manifest;
@Repository
public interface ManifestRepository extends JpaRepository<Manifest, Long> {
    List<Manifest> findByStatus(ManifestStatus status);
    boolean existsByTimetableId(Long timetableId);
}
