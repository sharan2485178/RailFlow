package com.example.repository;

import com.example.model.Yard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YardRepository extends JpaRepository<Yard, Long> {
    boolean existsByName(String name);
    Optional<Yard> findByName(String name);
}
