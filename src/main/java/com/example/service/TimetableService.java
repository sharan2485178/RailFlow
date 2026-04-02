package com.example.service;

import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.model.Locomotive;
import com.example.model.Timetable;
import com.example.model.TimetableStatus;
import com.example.model.Wagon;
import com.example.repository.LocomotiveRepository;
import com.example.repository.TimetableRepository;
import com.example.repository.WagonRepository;
import com.example.security.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableService {

    @Autowired private TimetableRepository timetableRepository;
    @Autowired private WagonRepository wagonRepository;
    @Autowired private LocomotiveRepository locomotiveRepository;
    @Autowired private AuditService auditService;

    /**
     * POST /api/timetables
     * Creates a new train timetable with assigned assets.
     */
    public Timetable create(TimetableRequest req, String performedBy) {
        Timetable timetable = new Timetable();
        timetable.setTrainId(req.getTrainId());
        timetable.setOrigin(req.getOrigin());
        timetable.setDestination(req.getDestination());
        timetable.setDepartureTime(req.getDepartureTime());
        timetable.setArrivalTime(req.getArrivalTime());
        timetable.setStatus(TimetableStatus.SCHEDULED);
        timetable.setWagonIdsJson(req.getWagonIdsJson());
        timetable.setLocomotiveIdsJson(req.getLocomotiveIdsJson());
        timetable.setCreatedAt(LocalDateTime.now());
        timetableRepository.save(timetable);

        auditService.log("CREATE_TIMETABLE", "Timetable", timetable.getId().toString(),
                performedBy, "Timetable created: " + req.getOrigin() + " → " + req.getDestination());
        return timetable;
    }

    /**
     * GET /api/timetables
     * Returns all timetables, optionally filtered by status.
     */
    public List<Timetable> getAll(String status) {
        if (status != null && !status.isBlank())
            return timetableRepository.findByStatus(TimetableStatus.valueOf(status.toUpperCase()));
        return timetableRepository.findAll();
    }

    /**
     * GET /api/timetables/{id}
     * Returns a single timetable by ID.
     */
    public Timetable getById(Long id) {
        return timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable not found: " + id));
    }

    /**
     * GET /api/timetables/{id}/assets
     * Returns the full list of wagons and locomotives assigned to a timetable.
     */
    public TimetableAssetsResponse getAssets(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timetable not found: " + id));

        List<Wagon> wagons = new ArrayList<>();
        if (timetable.getWagonIdsJson() != null && !timetable.getWagonIdsJson().isBlank()) {
            for (String wagonIdStr : timetable.getWagonIdsJson().split(",")) {
                String trimmed = wagonIdStr.trim();
                if (!trimmed.isEmpty()) {
                    Long wagonId = Long.parseLong(trimmed);
                    wagonRepository.findById(wagonId).ifPresent(wagons::add);
                }
            }
        }

        List<Locomotive> locomotives = new ArrayList<>();
        if (timetable.getLocomotiveIdsJson() != null && !timetable.getLocomotiveIdsJson().isBlank()) {
            for (String locIdStr : timetable.getLocomotiveIdsJson().split(",")) {
                String trimmed = locIdStr.trim();
                if (!trimmed.isEmpty()) {
                    Long locId = Long.parseLong(trimmed);
                    locomotiveRepository.findById(locId).ifPresent(locomotives::add);
                }
            }
        }

        TimetableAssetsResponse response = new TimetableAssetsResponse();
        response.setTimetableId(timetable.getId());
        response.setTrainId(timetable.getTrainId());
        response.setOrigin(timetable.getOrigin());
        response.setDestination(timetable.getDestination());
        response.setDepartureTime(timetable.getDepartureTime());
        response.setArrivalTime(timetable.getArrivalTime());
        response.setStatus(timetable.getStatus());
        response.setWagons(wagons);
        response.setLocomotives(locomotives);
        return response;
    }
}
