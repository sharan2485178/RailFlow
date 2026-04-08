package com.example.service;

import java.util.List;

import com.example.dto.YardSlotRequest;
import com.example.dto.YardSlotResponse;

public interface YardSlotService {
    YardSlotResponse configure(YardSlotRequest req, String performedBy);
    List<YardSlotResponse> getSlotMap();
    List<YardSlotResponse> getByYard(Long yardId);
}
