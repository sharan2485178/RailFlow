package com.example.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.NotificationCreateRequest;
import com.example.dto.PathConflictResponse;
import com.example.dto.ResolveConflictRequest;
import com.example.dto.TaskCreateRequest;
import com.example.enums.ConflictResolutionStatus;
import com.example.enums.NotificationCategory;
import com.example.enums.NotificationEntityType;
import com.example.enums.Role;
import com.example.enums.TimetableStatus;
import com.example.exception.EntityNotFoundException;
import com.example.exception.InvalidTimeRangeException;
import com.example.mapper.PathConflictMapper;
import com.example.model.PathConflict;
import com.example.model.Timetable;
import com.example.model.User;
import com.example.repository.PathConflictRepository;
import com.example.repository.TimetableRepository;
import com.example.repository.UserRepository;
import com.example.service.NotificationService;
import com.example.service.PathConflictService;
import com.example.service.TaskService;

import jakarta.transaction.Transactional;

@Service
public class PathConflictServiceImpl implements PathConflictService {

	private final PathConflictRepository pathConflictRepository;
	private final TimetableRepository timetableRepository;
	private final PathConflictMapper pathConflictMapper;
	private final UserRepository userRepository;
	private final NotificationService notificationService;
	private final TaskService taskService;

	public PathConflictServiceImpl(PathConflictRepository pathConflictRepository,
			TimetableRepository timetableRepository, PathConflictMapper pathConflictMapper, UserRepository userRepository,NotificationService notificationService,TaskService taskService) {
		this.pathConflictRepository = pathConflictRepository;
		this.timetableRepository = timetableRepository;
		this.pathConflictMapper = pathConflictMapper;
		this.userRepository=userRepository;
		this.notificationService=notificationService;
		this.taskService=taskService;
	}

	@Transactional
	public List<PathConflictResponse> getAllUnresolved() {
		return pathConflictRepository.findByConflictResolutionStatus(ConflictResolutionStatus.UNRESOLVED).stream()
				.map(pathConflictMapper::toDto).collect(Collectors.toList());
	}

	@Transactional
    public void detect(Timetable newTimetable) {
        List<Timetable> publishedTimetables = timetableRepository
                .findByStatus(TimetableStatus.PUBLISHED)
                .stream()
                .filter(t -> !t.getId().equals(newTimetable.getId()))
                .collect(Collectors.toList());

        for (Timetable published : publishedTimetables) {

            boolean samePathCode = published.getPathCode().equals(newTimetable.getPathCode());
            //Published: 10:00 to 14:00
            //New: 12:00 to 16:00
            // new departs 12:00 < published arrives 14:00 → true
            // new arrives 16:00 > published departs 10:00 → true (
            // if new train time is 6:00 to 9::00 condition 1 met ,to ensure new train arrival is after published train departure it is needed
            boolean timeOverlap  = newTimetable.getDepartureTime().isBefore(published.getArrivalTime())
                                && newTimetable.getArrivalTime().isAfter(published.getDepartureTime());

            if (samePathCode && timeOverlap) {
                // check both directions to avoid duplicate rows
                boolean alreadyExists =
                        pathConflictRepository.existsByTimetable1IdAndTimetable2Id(
                                newTimetable.getId(), published.getId())
                        || pathConflictRepository.existsByTimetable1IdAndTimetable2Id(
                                published.getId(), newTimetable.getId());

                if (!alreadyExists) {
                    PathConflict conflict = new PathConflict();
                    conflict.setTimetable1(newTimetable); // newly published → ON_HOLD
                    conflict.setTimetable2(published);     // existing published → untouched
                    conflict.setDetectedAt(LocalDateTime.now());
                    conflict.setConflictResolutionStatus(ConflictResolutionStatus.UNRESOLVED);
                    pathConflictRepository.save(conflict);

                    newTimetable.setStatus(TimetableStatus.ON_HOLD);
                    timetableRepository.save(newTimetable);

                    String conflictMsg = "Path conflict detected on route '"
                            + newTimetable.getPathCode()
                            + "' between timetable #" + newTimetable.getId()
                            + " and published timetable #" + published.getId();

                    notify(conflictMsg, conflict.getId());

                    // assign task to first available dispatcher
                    List<User> dispatchers = userRepository.findAll().stream()
                            .filter(u -> u.getRole() == Role.DISPATCHER)
                            .collect(Collectors.toList());
                    //if atleast one dispatcher is there
                    if (!dispatchers.isEmpty()) {
                        TaskCreateRequest taskReq = new TaskCreateRequest();
                        taskReq.setAssignedTo(dispatchers.get(0).getId());
                        taskReq.setDescription("Resolve path conflict #" + conflict.getId()
                                + " on route '" + newTimetable.getPathCode()
                                + "'. Timetable #" + newTimetable.getId()
                                + " is ON_HOLD pending resolution.");
                        taskReq.setRelatedEntityType(NotificationEntityType.TIMETABLE);
                        taskReq.setRelatedEntityId(conflict.getId());
                        taskReq.setDueDate(LocalDateTime.now().plusHours(24));
                        taskService.create(taskReq);
                    }
                }
            }
        }
    }

    // ── Resolve ───────────────────────────────────────────────────────────────
    // Dispatcher provides new times for the ON_HOLD timetable (t1).
    // 1. Update t1 times.
    // 2. Compare new times against ALL conflicts where timetable1 = t1.
    //    - If new times no longer overlap with that conflict's t2 → mark that conflict RESOLVED.
    //    - If still overlaps → keep UNRESOLVED.
    // 3. If ALL conflicts where timetable1 = t1 are now RESOLVED → draft t1.
    //    Otherwise → t1 stays ON_HOLD.

	@Transactional
	public PathConflictResponse resolve(Long id, ResolveConflictRequest req, String resolvedBy) {

	    PathConflict conflict = pathConflictRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("PathConflict", id));
        //Checking if the conflict is resolved already
	    if (conflict.getConflictResolutionStatus() == ConflictResolutionStatus.RESOLVED) {
	        throw new IllegalStateException("Conflict " + id + " is already resolved");
	    }
        //checking if departure time is before arrival time
	    if (!req.getNewDepartureTime().isBefore(req.getNewArrivalTime())) {
	        throw new InvalidTimeRangeException(req.getNewDepartureTime(),req.getNewArrivalTime());
	    }

	    Timetable t1 = conflict.getTimetable1();
	    //checking if timetable #1 is on ON_HOLD
	    if (t1.getStatus() != TimetableStatus.ON_HOLD) {
	        throw new IllegalStateException("Timetable #" + t1.getId()
	                + " is not ON_HOLD. Current status: " + t1.getStatus());
	    }

	    // apply new times to t1, keep ON_HOLD
	    t1.setDepartureTime(req.getNewDepartureTime());
	    t1.setArrivalTime(req.getNewArrivalTime());
	    timetableRepository.saveAndFlush(t1); // flush immediately to clear stale cache(persistent context)

	    // reload t1 fresh from DB after flush
	    Timetable freshT1 = timetableRepository.findById(t1.getId()).orElseThrow(() -> new EntityNotFoundException("Timetable Not Found", id));

	    // get ALL unresolved conflicts where timetable1 = t1
	    List<PathConflict> allConflictsForT1 = pathConflictRepository
	            .findByTimetable1Id(freshT1.getId())
	            .stream()
	            .filter(c -> c.getConflictResolutionStatus() == ConflictResolutionStatus.UNRESOLVED)
	            .collect(Collectors.toList());

	    // for each conflict, reload t2 fresh and check overlap with updated t1
	    for (PathConflict c : allConflictsForT1) {
	        // reload t2 fresh — lazy proxy may hold stale data
	        Timetable t2 = timetableRepository.findById(c.getTimetable2().getId()).orElseThrow(() -> new EntityNotFoundException("Timetable Not Found", id));

	        boolean stillOverlaps = freshT1.getPathCode().equals(t2.getPathCode())
	                && freshT1.getDepartureTime().isBefore(t2.getArrivalTime())
	                && freshT1.getArrivalTime().isAfter(t2.getDepartureTime());
            //if not overlapping resolve the conflict
	        if (!stillOverlaps) {
	            c.setConflictResolutionStatus(ConflictResolutionStatus.RESOLVED);
	            //auto-resolved if conflict-id is not same
	            c.setResolutionNote(c.getId().equals(id)
	                    ? req.getResolutionNote()
	                    : "Auto-resolved: timetable #" + freshT1.getId()
	                            + " rescheduled, no longer overlaps with timetable #" + t2.getId());
	            c.setResolvedBy(resolvedBy);
	            c.setResolvedAt(LocalDateTime.now());
	            pathConflictRepository.save(c);
	        }
	    }

	    // check if ALL conflicts for t1 are now resolved
	    boolean allResolved = pathConflictRepository
	            .findByTimetable1Id(freshT1.getId())
	            .stream()
	            .allMatch(c -> c.getConflictResolutionStatus() == ConflictResolutionStatus.RESOLVED);

	    if (allResolved) {
	    	    //if all resolved update to DRAFT
	        freshT1.setStatus(TimetableStatus.DRAFT);
	        timetableRepository.save(freshT1);
          
	        
	        notify("All conflicts for timetable #" + freshT1.getId() + " on route '"
	                + freshT1.getPathCode() + "' resolved by " + resolvedBy
	                + ". Timetable rescheduled to " + req.getNewDepartureTime()
	                + " → " + req.getNewArrivalTime() + " and is now PUBLISHED.",
	                conflict.getId());
	    } else {
	    	    //Count the No.of Unresolved Conflicts
	        long remaining = pathConflictRepository.findByTimetable1Id(freshT1.getId())
	                .stream()
	                .filter(c -> c.getConflictResolutionStatus() == ConflictResolutionStatus.UNRESOLVED)
	                .count();
            //notify to all dispatchers/admin
	        notify("Partial resolution for timetable #" + freshT1.getId() + " on route '"
	                + freshT1.getPathCode() + "'. " + remaining
	                + " conflict(s) still unresolved. Timetable remains ON_HOLD.",
	                conflict.getId());
	    }

	    return pathConflictMapper.toDto(conflict);
	}

    // Helper Functions

    

    private void notify(String message, Long conflictId) {
        List<User> recipients = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.DISPATCHER || u.getRole() == Role.ADMIN)
                .collect(Collectors.toList());

        for (User user : recipients) {
            NotificationCreateRequest notifReq = new NotificationCreateRequest();
            notifReq.setUserId(user.getId());
            notifReq.setRole(user.getRole());
            notifReq.setMessage(message);
            notifReq.setEntityId(conflictId);
            notifReq.setEntityType(NotificationEntityType.TIMETABLE);
            notifReq.setCategory(NotificationCategory.CONFLICT);
            notificationService.create(notifReq);
        }
    }
}
