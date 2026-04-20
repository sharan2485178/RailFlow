package com.example.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.enums.AssetAssignmentStatus;
import com.example.exception.AssetAlreadyAssignedException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.WagonAssignmentMapper;
import com.example.model.Wagon;
import com.example.model.WagonAssignment;
import com.example.repository.WagonAssignmentRepository;
import com.example.repository.WagonRepository;
import com.example.service.WagonAssignmentService;

@Service
public class WagonAssignmentServiceImpl implements WagonAssignmentService {

    @Autowired
    private WagonAssignmentRepository wagonAssignmentRepository;

    @Autowired
    private WagonAssignmentMapper wagonAssignmentMapper;

    @Autowired
    private WagonRepository wagonRepository;
    
    public WagonAssignmentResponse assign(WagonAssignmentRequest req) {
        if (wagonAssignmentRepository.existsByBookingIdAndTimetableId(req.getBookingId(), req.getTimetableId())) {
            throw new IllegalStateException(
                    "Booking " + req.getBookingId()
                    + " is already assigned to a wagon");
        }
        Wagon wagon=wagonRepository.findById(req.getWagonId()).orElseThrow(()->new EntityNotFoundException("Wagon with id"+req.getWagonId()+" is not found"));
        
        boolean assigned=wagonAssignmentRepository.existsByWagonIdAndAssetOperationalStatusIn(req.getWagonId(),List.of(AssetAssignmentStatus.PENDING,AssetAssignmentStatus.CONFIRMED,AssetAssignmentStatus.IN_TRANSIT));
        
        if(assigned) {
        	   throw new AssetAlreadyAssignedException("Wagon id"+req.getWagonId()+" is already assigned");
        }
        WagonAssignment wagonAssignment = wagonAssignmentMapper.toEntity(req);
        wagonAssignmentRepository.save(wagonAssignment);

        return wagonAssignmentMapper.toDto(wagonAssignment);
    }
}
