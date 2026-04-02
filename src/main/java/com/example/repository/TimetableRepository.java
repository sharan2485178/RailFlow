package com.example.repository;

import com.example.model.Timetable;
import com.example.model.TimetableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByTrainId(Long trainId);
    List<Timetable> findByStatus(TimetableStatus status);
}
