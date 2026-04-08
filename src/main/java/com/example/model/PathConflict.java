package com.example.model;

import java.time.LocalDateTime;

import com.example.enums.ConflictResolutionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="path_conflict")
public class PathConflict {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="timetable_id1")
	private Timetable timetable1;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="timetable_id2")
	private Timetable timetable2;
	
	@Column(nullable=false,updatable=false)
	private LocalDateTime detectedAt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ConflictResolutionStatus conflictResolutionStatus=ConflictResolutionStatus.UNRESOLVED;
	
	@Column(columnDefinition = "TEXT")
    private String resolutionNote;

    private String resolvedBy;

    private LocalDateTime resolvedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timetable getTimetable1() {
		return timetable1;
	}

	public void setTimetable1(Timetable timetable1) {
		this.timetable1 = timetable1;
	}

	public Timetable getTimetable2() {
		return timetable2;
	}

	public void setTimetable2(Timetable timetable2) {
		this.timetable2 = timetable2;
	}

	public LocalDateTime getDetectedAt() {
		return detectedAt;
	}

	public void setDetectedAt(LocalDateTime detectedAt) {
		this.detectedAt = detectedAt;
	}

	public ConflictResolutionStatus getConflictResolutionStatus() {
		return conflictResolutionStatus;
	}

	public void setConflictResolutionStatus(ConflictResolutionStatus conflictResolutionStatus) {
		this.conflictResolutionStatus = conflictResolutionStatus;
	}

	public String getResolutionNote() {
		return resolutionNote;
	}

	public void setResolutionNote(String resolutionNote) {
		this.resolutionNote = resolutionNote;
	}

	public String getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(String resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public PathConflict(Long id, Timetable timetable1, Timetable timetable2, LocalDateTime detectedAt,
			ConflictResolutionStatus conflictResolutionStatus, String resolutionNote, String resolvedBy,
			LocalDateTime resolvedAt) {
		super();
		this.id = id;
		this.timetable1 = timetable1;
		this.timetable2 = timetable2;
		this.detectedAt = detectedAt;
		this.conflictResolutionStatus = conflictResolutionStatus;
		this.resolutionNote = resolutionNote;
		this.resolvedBy = resolvedBy;
		this.resolvedAt = resolvedAt;
	}

	public PathConflict() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
    
	
	

}
