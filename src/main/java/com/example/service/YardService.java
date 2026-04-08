package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.YardRequest;
import com.example.dto.YardResponse;

@Service
public interface YardService {
	
	public YardResponse create(YardRequest req, String performedBy);
	public List<YardResponse> getAll();

}
