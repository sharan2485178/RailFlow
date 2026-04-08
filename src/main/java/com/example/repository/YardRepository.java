package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Yard;
@Repository
public interface YardRepository extends JpaRepository<Yard, Long> {
    boolean existsByName(String name);
    Optional<Yard> findByName(String name);
}
