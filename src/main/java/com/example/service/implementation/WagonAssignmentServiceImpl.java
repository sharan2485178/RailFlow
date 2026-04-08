package com.example.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.mapper.WagonAssignmentMapper;
import com.example.model.WagonAssignment;
import com.example.repository.WagonAssignmentRepository;
import com.example.service.WagonAssignmentService;

@Service
public class WagonAssignmentServiceImpl implements WagonAssignmentService {

    @Autowired
    private WagonAssignmentRepository wagonAssignmentRepository;

    @Autowired
    private WagonAssignmentMapper wagonAssignmentMapper;

    public WagonAssignmentResponse assign(WagonAssignmentRequest req) {
        if (wagonAssignmentRepository.existsByBookingIdAndTimetableId(req.getBookingId(), req.getTimetableId())) {
            throw new IllegalStateException(
                    "Booking " + req.getBookingId()
                    + " is already assigned to a wagon");
        }

        WagonAssignment wagonAssignment = wagonAssignmentMapper.toEntity(req);
        wagonAssignmentRepository.save(wagonAssignment);

        return wagonAssignmentMapper.toDto(wagonAssignment);
    }
}
