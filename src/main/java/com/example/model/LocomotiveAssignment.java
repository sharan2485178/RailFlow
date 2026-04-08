package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.enums.AssetAssignmentStatus;

@Entity
@Table(name = "locomotive_assignment")
public class LocomotiveAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id", nullable = false)
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locomotive_id", nullable = false)
    private Locomotive locomotive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetAssignmentStatus status = AssetAssignmentStatus.PENDING;



    public LocomotiveAssignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Timetable getTimetable() { return timetable; }
    public void setTimetable(Timetable timetable) { this.timetable = timetable; }

    public Locomotive getLocomotive() { return locomotive; }
    public void setLocomotive(Locomotive locomotive) { this.locomotive = locomotive; }

    public AssetAssignmentStatus getStatus() { return status; }
    public void setStatus(AssetAssignmentStatus status) { this.status = status; }

	@Override
	public String toString() {
		return "LocomotiveAssignment [id=" + id + ", timetable=" + timetable + ", locomotive=" + locomotive
				+ ", status=" + status + "]";
	}


    
}