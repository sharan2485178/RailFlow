package com.example.model;

import com.example.enums.ManifestBookingStatus;

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
@Table(name="manifest_booking")
public class ManifestBooking {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="manifest_id")
    private Manifest manifest;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ManifestBookingStatus manifestBookingStatus = ManifestBookingStatus.PENDING;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Manifest getManifest() {
		return manifest;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public ManifestBookingStatus getManifestBookingStatus() {
		return manifestBookingStatus;
	}

	public void setManifestBookingStatus(ManifestBookingStatus manifestBookingStatus) {
		this.manifestBookingStatus = manifestBookingStatus;
	}

	public ManifestBooking(Long id, Manifest manifest, Booking booking, ManifestBookingStatus manifestBookingStatus) {
		super();
		this.id = id;
		this.manifest = manifest;
		this.booking = booking;
		this.manifestBookingStatus = manifestBookingStatus;
	}

	public ManifestBooking() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
    
    
    
    
}
