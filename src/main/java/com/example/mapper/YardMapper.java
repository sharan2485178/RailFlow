package com.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.dto.YardRequest;
import com.example.dto.YardResponse;
import com.example.model.Yard;

@Component
public class YardMapper {

    public Yard toEntity(YardRequest req) {
        Yard yard = new Yard();
        yard.setName(req.getName());
        yard.setLocation(req.getLocation());
        yard.setTotalSlots(req.getTotalSlots());
        yard.setCreatedAt(LocalDateTime.now());
        return yard;
    }

    public YardResponse toResponse(Yard yard) {
        YardResponse response = new YardResponse();
        response.setId(yard.getId());
        response.setName(yard.getName());
        response.setLocation(yard.getLocation());
        response.setTotalSlots(yard.getTotalSlots());
        response.setCreatedAt(yard.getCreatedAt());
        return response;
    }
}