package com.example.service;

import java.util.List;

import com.example.dto.WagonRequest;
import com.example.dto.WagonResponse;
import com.example.dto.WagonStatusRequest;

public interface WagonService {
    WagonResponse register(WagonRequest req, String performedBy);
    List<WagonResponse> getAll(String type, String status);
    WagonResponse getById(Long id);
    WagonResponse update(Long id, WagonRequest req, String performedBy);
    WagonResponse changeStatus(Long id, WagonStatusRequest req, String performedBy);
}
