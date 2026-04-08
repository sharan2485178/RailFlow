package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.TrainStatus;
import com.example.model.Train;
@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    boolean existsByNumber(String number);
    Optional<Train> findByNumber(String number);
    List<Train> findByStatus(TrainStatus status);
}
