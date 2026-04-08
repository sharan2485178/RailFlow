package com.example.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.dto.TimetableResponse;
import com.example.dto.TimetableStatusRequest;
import com.example.dto.TimetableUpdateRequest;
import com.example.enums.TimetableStatus;
import com.example.enums.TrainStatus;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.TimetableMapper;
import com.example.model.Locomotive;
import com.example.model.LocomotiveAssignment;
import com.example.model.Timetable;
import com.example.model.Wagon;
import com.example.model.WagonAssignment;
import com.example.repository.LocomotiveAssignmentRepository;
import com.example.repository.LocomotiveRepository;
import com.example.repository.TimetableRepository;
import com.example.repository.TrainRepository;
import com.example.repository.WagonAssignmentRepository;
import com.example.repository.WagonRepository;
import com.example.security.AuditService;
import com.example.service.PathConflictService;
import com.example.service.TimetableService;

import jakarta.transaction.Transactional;

@Service
public class TimetableServiceImpl implements TimetableService {

	@Autowired
	private TimetableRepository timetableRepository;
	@Autowired
	private WagonRepository wagonRepository;
	@Autowired
	private LocomotiveRepository locomotiveRepository;
	@Autowired
	private AuditService auditService;

	@Autowired
	private PathConflictService pathConflictService;

	@Autowired
	private TrainRepository trainRepository;

	@Autowired
	private TimetableMapper timetableMapper;

	@Autowired
	private WagonAssignmentRepository wagonAssignmentRepository;

	@Autowired
	private LocomotiveAssignmentRepository locomotiveAssignmentRepository;

	@Transactional
	public TimetableResponse create(TimetableRequest req, String perfomedBy) {

		if (!req.getDepartureTime().isBefore(req.getArrivalTime())) {
			throw new IllegalArgumentException("Departure time must be before arrival time");
		}

		Timetable timetable = timetableMapper.toEntity(req);

		if (timetable.getTrain().getStatus() != TrainStatus.ACTIVE) {
			throw new IllegalStateException("Train is not active. Current status: " + timetable.getTrain().getStatus());
		}
		timetableRepository.save(timetable);

		auditService.log("CREATE_TIMETABLE", "Timetable", timetable.getId().toString(), perfomedBy,
				"Timetable created for train " + req.getTrainId());

		pathConflictService.detect(timetable);

		return timetableMapper.toDto(timetable);
	}

	@Transactional
	public List<TimetableResponse> getAll(String status) {
		List<Timetable> timetables;
		if (status != null && status.isBlank()) {
			TimetableStatus timetableStatus;
			try {
				timetableStatus = TimetableStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid status: " + status + ". Allowed values: "
						+ java.util.Arrays.toString(TimetableStatus.values()));
			}

			timetables = timetableRepository.findByStatus(timetableStatus);

		} else {
			timetables = timetableRepository.findAll();
		}
		return timetables.stream().map(timetableMapper::toDto).collect(Collectors.toList());
	}

	public Timetable getById(Long id) {
		return timetableRepository.findById(id).orElseThrow(() -> new RuntimeException("Timetable not found: " + id));
	}

	@Transactional
	public TimetableAssetsResponse getAssets(Long id) {

		Timetable timetable = timetableRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Timetable", id));

		List<Wagon> wagons = wagonAssignmentRepository.findByTimetableId(id).stream().map(WagonAssignment::getWagon)
				.collect(Collectors.toList());

		List<Locomotive> locomotives = locomotiveAssignmentRepository.findByTimetableId(id).stream()
				.map(LocomotiveAssignment::getLocomotive).collect(Collectors.toList());

		TimetableAssetsResponse response = new TimetableAssetsResponse();
		response.setTimetableId(timetable.getId());
		response.setTrainId(timetable.getTrain().getId());
		response.setDepartureTime(timetable.getDepartureTime());
		response.setArrivalTime(timetable.getArrivalTime());
		response.setWagons(wagons);
		response.setLocomotives(locomotives);

		return response;
	}

	@Transactional
	public TimetableResponse update(Long id, TimetableUpdateRequest req, String performedBy) {
		Timetable timetable = timetableRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Timetable", id));

		if (timetable.getStatus() != TimetableStatus.DRAFT && timetable.getStatus() != TimetableStatus.ON_HOLD) {
			throw new IllegalStateException("Cannot update timetable in status: " + timetable.getStatus());
		}

		if (!req.getDepartureTime().isBefore(req.getArrivalTime())) {
			throw new IllegalArgumentException("Departure time must be before arrival time");
		}

		timetableMapper.toUpdate(timetable, req);

		timetableRepository.save(timetable);

		auditService.log("UPDATE_TIMETABLE", "Timetable", timetable.getId().toString(), performedBy,
				"Timetable updated | path: " + req.getPathCode());

		return timetableMapper.toDto(timetable);
	}

	@Transactional
	public TimetableResponse updateStatus(Long id, TimetableStatusRequest req, String performedBy) {

		Timetable timetable = timetableRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Timetable", id));

		TimetableStatus currentStatus = timetable.getStatus();
		TimetableStatus newStatus = req.getStatus();

		// validate allowed transitions
		validateStatusTransition(currentStatus, newStatus, id);

		timetable.setStatus(newStatus);
		timetableRepository.save(timetable);

		// if publishing — run conflict detection
		if (newStatus == TimetableStatus.PUBLISHED) {
			pathConflictService.detect(timetable);
		}

		auditService.log("UPDATE_TIMETABLE_STATUS", "Timetable", id.toString(), performedBy,
				"Status changed: " + currentStatus + " → " + newStatus);

		return timetableMapper.toDto(timetable);
	}

	// ── Allowed Status Transitions ────────────────────────────────────────────
	//
	// DRAFT → PUBLISHED, CANCELLED
	// PUBLISHED → CANCELLED
	// ON_HOLD → CANCELLED (cannot manually publish — only resolved via
	// PathConflict)
	// CANCELLED → (terminal — no transitions allowed)

	private void validateStatusTransition(TimetableStatus current, TimetableStatus next, Long id) {
		boolean allowed = switch (current) {
		case DRAFT -> next == TimetableStatus.PUBLISHED || next == TimetableStatus.CANCELLED;
		case PUBLISHED -> next == TimetableStatus.CANCELLED;
		case ON_HOLD -> next == TimetableStatus.CANCELLED;
		case CANCELLED -> false;
		default -> false;
		};

		if (!allowed) {
			throw new IllegalStateException("Timetable #" + id + " cannot transition from " + current + " to " + next);
		}
	}
}
