package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.CrewAssignmentRequest;
import com.example.dto.CrewAssignmentResponse;
import com.example.dto.UserResponse;
import com.example.enums.CrewAssignmentStatus;
import com.example.enums.Role;
import com.example.mapper.CrewAssignmentMapper;
import com.example.mapper.UserMapper;
import com.example.model.CrewAssignment;
import com.example.model.User;
import com.example.repository.CrewAssignmentRepository;
import com.example.repository.UserRepository;
import com.example.service.CrewAssignmentService;

@Service
public class CrewAssignmentServiceImpl implements CrewAssignmentService {

	private final CrewAssignmentRepository crewAssignmentRepository;
	private final CrewAssignmentMapper crewAssignmentMapper;
	private final UserRepository userRepository;

	private final UserMapper userMapper;

	public CrewAssignmentServiceImpl(CrewAssignmentRepository crewAssignmentRepository,
			CrewAssignmentMapper crewAssignmentMapper, UserRepository userRepository, UserMapper userMapper) {
		this.crewAssignmentRepository = crewAssignmentRepository;
		this.crewAssignmentMapper = crewAssignmentMapper;
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Transactional
	public CrewAssignmentResponse assign(CrewAssignmentRequest req) {

		// one role per timetable
		if (crewAssignmentRepository.existsByTimetableIdAndCrewRole(req.getTimetableId(), req.getCrewRole())) {
			throw new IllegalStateException("Role " + req.getCrewRole() + " is already assigned for this timetable");
		}

		CrewAssignment assignment = crewAssignmentMapper.toEntity(req);
		crewAssignmentRepository.save(assignment);
		return crewAssignmentMapper.toDto(assignment);
	}

	@Transactional(readOnly = true)
	public List<CrewAssignmentResponse> getByTimetable(Long timetableId) {
		return crewAssignmentRepository.findByTimetableId(timetableId).stream().map(crewAssignmentMapper::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public CrewAssignmentResponse confirm(Long assignmentId, String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found: " + email));

		CrewAssignment assignment = crewAssignmentRepository.findByIdAndUserId(assignmentId, user.getId())
				.orElseThrow(() -> new IllegalStateException("Assignment not found or does not belong to you"));

		if (assignment.getStatus() != CrewAssignmentStatus.PENDING) {
			throw new IllegalStateException("Assignment is already " + assignment.getStatus());
		}

		assignment.setStatus(CrewAssignmentStatus.ACKNOWLEDGED);
		crewAssignmentRepository.save(assignment);
		return crewAssignmentMapper.toDto(assignment);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> getAvailableCrew() {
		return userRepository.findByRoleIn(List.of(Role.LOCOMOTIVE_ENGINEER, Role.MAINTENANCE_CREW, Role.YARD_MANAGER))
				.stream().map(userMapper::toDto).collect(Collectors.toList());
	}
}
