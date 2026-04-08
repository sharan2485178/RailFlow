package com.example.model;

import java.time.LocalDateTime;

import com.example.enums.AssetAssignmentStatus;
import com.example.enums.CrewAssignmentStatus;
import com.example.enums.Role;

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
@Table(name = "crew_assignment")
public class CrewAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id", nullable = false)
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "crew_role", nullable = false)
    private Role crewRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrewAssignmentStatus status = CrewAssignmentStatus.PENDING;

    public CrewAssignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Timetable getTimetable() { return timetable; }
    public void setTimetable(Timetable timetable) { this.timetable = timetable; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Role getCrewRole() { return crewRole; }
    public void setCrewRole(Role crewRole) { this.crewRole = crewRole; }

	public CrewAssignmentStatus getStatus() {
		return status;
	}

	public void setStatus(CrewAssignmentStatus status) {
		this.status = status;
	}

    
}