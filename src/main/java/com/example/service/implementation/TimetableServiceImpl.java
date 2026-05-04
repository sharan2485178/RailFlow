package com.example.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dto.LocomotiveDTO;
import com.example.dto.PageResponse;
import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.dto.TimetableResponse;
import com.example.dto.TimetableStatusRequest;
import com.example.dto.TimetableUpdateRequest;
import com.example.dto.WagonDTO;
import com.example.enums.AssetType;
import com.example.enums.TimetableStatus;
import com.example.enums.TrainStatus;
import com.example.exception.EntityNotFoundException;
import com.example.exception.InvalidTimeRangeException;
import com.example.mapper.TimetableMapper;
import com.example.model.LocomotiveAssignment;
import com.example.model.Timetable;
import com.example.model.WagonAssignment;
import com.example.model.YardSlot;
import com.example.repository.LocomotiveAssignmentRepository;
import com.example.repository.LocomotiveRepository;
import com.example.repository.TimetableRepository;
import com.example.repository.TrainRepository;
import com.example.repository.WagonAssignmentRepository;
import com.example.repository.WagonRepository;
import com.example.repository.YardSlotRepository;
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
	
	@Autowired
	private YardSlotRepository yardSlotRepository;

	@Transactional
	public TimetableResponse create(TimetableRequest req, String perfomedBy) {

		if (!req.getDepartureTime().isBefore(req.getArrivalTime())) {
			throw new InvalidTimeRangeException(req.getDepartureTime(),req.getArrivalTime());
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
		if (status != null && !status.isBlank()) {
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

	
	@Transactional
	public TimetableAssetsResponse getAssets(Long id) {

	    Timetable timetable = timetableRepository.findById(id) // ✅ fixed typo
	            .orElseThrow(() -> new EntityNotFoundException("Timetable", id));

	    List<WagonDTO> wagons = wagonAssignmentRepository.findByTimetableId(id).stream()
	            .map(WagonAssignment::getWagon) //fetches wagon based on wagonId
	            .map(w -> { //block lambda - used when there is a multiple statements and require return statement
	            	  
	            	   Optional<YardSlot> slot=yardSlotRepository.findByAssignedAssetTypeAndAssignedAssetId(AssetType.WAGON,w.getId());
	            	   Long yardId=slot.map(y->y.getYard().getId()).orElse(null);
	            	   Long slotId=slot.map(y->y.getSlotId()).orElse(null);
	            	   String trackNumber=slot.map(y->y.getTrackNumber()).orElse("Wagon not present in any yard slot");
	            	   int position=slot.map(y->y.getPosition()).orElse(0);
	            	   return new WagonDTO(w.getId(),w.getSerialNumber(),w.getType(),yardId,slotId,trackNumber,position);
	             })
	            .collect(Collectors.toList());

	    List<LocomotiveDTO> locomotives = locomotiveAssignmentRepository.findByTimetableId(id).stream()
	            .map(LocomotiveAssignment::getLocomotive)
	            .map(l -> {
	            	    Optional<YardSlot>slot=yardSlotRepository.findByAssignedAssetTypeAndAssignedAssetId(AssetType.LOCOMOTIVE, l.getId());
	            	    Long yardId=slot.map(x->x.getYard().getId()).orElse(null);
	            	    Long slotId=slot.map(x->x.getSlotId()).orElse(null);
	            	    String trackNumber=slot.map(x->x.getTrackNumber()).orElse("Locomotive not present");
	            	    int position=slot.map(x->x.getPosition()).orElse(0);
	            	    return new LocomotiveDTO(l.getId(),l.getSerialNumber(),l.getModel(),yardId,slotId,trackNumber,position);
	            })
	            .collect(Collectors.toList());
	    
        
	   
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
		
        //ON_HOLD & DRAFT Timetable can only updated
		if (timetable.getStatus() != TimetableStatus.DRAFT && timetable.getStatus() != TimetableStatus.ON_HOLD) {
			throw new IllegalStateException("Cannot update timetable in status: " + timetable.getStatus());
		}

		if (!req.getDepartureTime().isBefore(req.getArrivalTime())) {
			throw new InvalidTimeRangeException(req.getDepartureTime(),req.getArrivalTime());
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
	
	public PageResponse<TimetableResponse> getAllByPageAndSort(int page,int size,String sortBy){
		
		
	    Sort sort=Sort.by(sortBy).ascending();
		Pageable pageable=PageRequest.of(page, size,sort);
		
		Page<Timetable> timetablePage = timetableRepository.findAll(pageable);
		return PageResponse.from(timetablePage.map(timetableMapper::toDto));
	}
	
	
}
