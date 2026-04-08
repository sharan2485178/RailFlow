package com.example.service.implementation;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.YardSlotRequest;
import com.example.dto.YardSlotResponse;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.YardSlotMapper;
import com.example.model.Yard;
import com.example.model.YardSlot;
import com.example.repository.YardRepository;
import com.example.repository.YardSlotRepository;
import com.example.service.YardSlotService;

@Service
public class YardSlotServiceImpl implements YardSlotService {

    private final YardSlotRepository yardSlotRepository;
    private final YardRepository yardRepository;
    private final YardSlotMapper yardSlotMapper;

    public YardSlotServiceImpl(YardSlotRepository yardSlotRepository,
                               YardRepository yardRepository,
                               YardSlotMapper yardSlotMapper) {
        this.yardSlotRepository = yardSlotRepository;
        this.yardRepository = yardRepository;
        this.yardSlotMapper = yardSlotMapper;
    }

    @Transactional
    public YardSlotResponse configure(YardSlotRequest req, String performedBy) {
        // find yard
        Yard yard = yardRepository.findById(req.getYardId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Yard", req.getYardId()));

        // check duplicate — same track and position in same yard
        if (yardSlotRepository.existsByYardIdAndTrackNumberAndPosition(
                req.getYardId(), req.getTrackNumber(), req.getPosition())) {
            throw new IllegalStateException(
                    "Slot already exists at track " + req.getTrackNumber()
                    + " position " + req.getPosition()
                    + " in yard " + yard.getName());
        }

        // build and save slot
        YardSlot slot = yardSlotMapper.toEntity(req, yard);
        yardSlotRepository.save(slot);

        return yardSlotMapper.toResponse(slot);
    }

    @Transactional(readOnly = true)
    public List<YardSlotResponse> getSlotMap() {
        return yardSlotRepository.findAll()
                .stream()
                .map(yardSlotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YardSlotResponse> getByYard(Long yardId) {
        return yardSlotRepository.findByYardId(yardId)
                .stream()
                .map(yardSlotMapper::toResponse)
                .collect(Collectors.toList());
    }
}