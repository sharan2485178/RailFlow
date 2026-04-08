package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.enums.ConflictResolutionStatus;
import com.example.model.PathConflict;

@Repository
public interface PathConflictRepository extends JpaRepository<PathConflict, Long> {

    // all unresolved conflicts
    List<PathConflict> findByConflictResolutionStatus(ConflictResolutionStatus status);

    
    List<PathConflict>findByTimetable1Id(Long timetableId1);
    // conflicts for a specific timetable — check both sides
    List<PathConflict> findByTimetable1IdOrTimetable2Id(Long timetableId1, Long timetableId2);

    // check if an UNRESOLVED conflict already exists between two timetables
    boolean existsByTimetable1IdAndTimetable2Id(Long timetableId1, Long timetableId2);

    // check if timetable has any OTHER unresolved conflicts excluding the current one
    @Query("""
            SELECT COUNT(c) > 0 FROM PathConflict c
            WHERE c.conflictResolutionStatus = 'UNRESOLVED'
            AND c.id <> :excludeConflictId
            AND (c.timetable1.id = :timetableId OR c.timetable2.id = :timetableId)
            """)
    boolean existsOtherUnresolvedConflicts(
            @Param("timetableId") Long timetableId,
            @Param("excludeConflictId") Long excludeConflictId);
}