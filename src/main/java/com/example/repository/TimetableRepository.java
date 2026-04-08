package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.enums.TimetableStatus;
import com.example.model.Timetable;
@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByTrainId(Long trainId);
    List<Timetable> findByStatus(TimetableStatus status);
}
