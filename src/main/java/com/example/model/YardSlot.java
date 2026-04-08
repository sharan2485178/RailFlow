package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.example.enums.AssetType;
import com.example.enums.YardSlotStatus;

@Entity
@Table(name = "yard_slot")
@Data
public class YardSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yard_id", nullable = false)
    private Yard yard;


    @Column(nullable = false)
    private String trackNumber;

    @Column(nullable=false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YardSlotStatus status = YardSlotStatus.AVAILABLE;

    // Asset currently occupying this slot (nullable when slot is free)
    @Enumerated(EnumType.STRING)
    private AssetType assignedAssetType;

    private Long assignedAssetId;

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }


    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public YardSlotStatus getStatus() {
        return status;
    }

    public void setStatus(YardSlotStatus status) {
        this.status = status;
    }

    public AssetType getAssignedAssetType() {
        return assignedAssetType;
    }

    public void setAssignedAssetType(AssetType assignedAssetType) {
        this.assignedAssetType = assignedAssetType;
    }

    public Long getAssignedAssetId() {
        return assignedAssetId;
    }

    public void setAssignedAssetId(Long assignedAssetId) {
        this.assignedAssetId = assignedAssetId;
    }

	public Yard getYard() {
		return yard;
	}

	public void setYard(Yard yard) {
		this.yard = yard;
	}
    
    

    

}
