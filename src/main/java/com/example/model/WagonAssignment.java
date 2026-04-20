package com.example.model;

import com.example.enums.AssetAssignmentStatus;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="wagon_assignment")
public class WagonAssignment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="timetable_id")
	private Timetable timetable;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="booking_id")
	private Booking booking;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="wagon_id")
	private Wagon wagon;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private AssetAssignmentStatus assetAssignmentStatus=AssetAssignmentStatus.PENDING; //PENDING,CONFIRMED,IN_TRANSIT, COMPLETED, CANCELED

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timetable getTimetable() {
		return timetable;
	}

	public void setTimetable(Timetable timetable) {
		this.timetable = timetable;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Wagon getWagon() {
		return wagon;
	}

	public void setWagon(Wagon wagon) {
		this.wagon = wagon;
	}

	public AssetAssignmentStatus getAssetAssignmentStatus() {
		return assetAssignmentStatus;
	}

	public void setAssetAssignmentStatus(AssetAssignmentStatus assetAssignmentStatus) {
		this.assetAssignmentStatus = assetAssignmentStatus;
	}

	public WagonAssignment(Long id, Timetable timetable, Booking booking, Wagon wagon,
			AssetAssignmentStatus assetAssignmentStatus) {
		super();
		this.id = id;
		this.timetable = timetable;
		this.booking = booking;
		this.wagon = wagon;
		this.assetAssignmentStatus = assetAssignmentStatus;
	}

	@Override
	public String toString() {
		return "WagonAssignment [id=" + id + ", timetable=" + timetable + ", booking=" + booking + ", wagon=" + wagon
				+ ", assetAssignmentStatus=" + assetAssignmentStatus + "]";
	}

	public WagonAssignment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
