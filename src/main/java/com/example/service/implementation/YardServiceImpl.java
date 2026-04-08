package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.YardRequest;
import com.example.dto.YardResponse;
import com.example.mapper.YardMapper;
import com.example.model.Yard;
import com.example.repository.YardRepository;
import com.example.service.YardService;

@Service
public class YardServiceImpl implements YardService {

    private final YardRepository yardRepository;
    private final YardMapper yardMapper;

    public YardServiceImpl(YardRepository yardRepository, YardMapper yardMapper) {
        this.yardRepository = yardRepository;
        this.yardMapper = yardMapper;
    }

    @Transactional
    public YardResponse create(YardRequest req, String performedBy) {
        if (yardRepository.existsByName(req.getName())) {
            throw new RuntimeException("Yard with name '" + req.getName() + "' already exists");
        }
        Yard yard = yardMapper.toEntity(req);
        yardRepository.save(yard);
        return yardMapper.toResponse(yard);
    }

    @Transactional(readOnly = true)
    public List<YardResponse> getAll() {
        return yardRepository.findAll()
                .stream()
                .map(yardMapper::toResponse)
                .collect(Collectors.toList());
    }
}