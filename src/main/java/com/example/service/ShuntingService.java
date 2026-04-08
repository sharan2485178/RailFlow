package com.example.service;

import java.util.List;

import com.example.dto.AssignSlotRequest;
import com.example.dto.ShuntingRequest;
import com.example.dto.ShuntingResponse;
import com.example.dto.YardSlotResponse;

public interface ShuntingService {
    ShuntingResponse shunt(ShuntingRequest req, String performedBy);
    YardSlotResponse assignSlot(Long yardId, Long slotId, AssignSlotRequest req, String performedBy);
    YardSlotResponse releaseSlot(Long yardId, Long slotId, String performedBy);
    List<YardSlotResponse> getSlotsByYard(Long yardId);
}