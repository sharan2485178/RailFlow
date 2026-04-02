package com.example.repository;

import com.example.model.Train;
import com.example.model.TrainStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    boolean existsByNumber(String number);
    Optional<Train> findByNumber(String number);
    List<Train> findByStatus(TrainStatus status);
}
