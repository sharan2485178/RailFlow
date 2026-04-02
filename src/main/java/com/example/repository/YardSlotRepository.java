package com.example.repository;

import com.example.model.YardSlot;
import com.example.model.YardSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YardSlotRepository extends JpaRepository<YardSlot, Long> {
    boolean existsBySlotCode(String slotCode);
    List<YardSlot> findByStatus(YardSlotStatus status);
    List<YardSlot> findByYardName(String yardName);
}
