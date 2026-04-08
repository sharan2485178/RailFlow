package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.TimetableRequest;
import com.example.dto.TimetableResponse;
import com.example.dto.TimetableUpdateRequest;
import com.example.enums.TimetableStatus;
import com.example.exception.EntityNotFoundException;
import com.example.model.Timetable;
import com.example.model.Train;
import com.example.repository.TrainRepository;
@Component
public class TimetableMapper {
	
	 private final TrainRepository trainRepository;

	    public TimetableMapper(TrainRepository trainRepository) {
	        this.trainRepository = trainRepository;
	    }
	
	public TimetableResponse toDto(Timetable timetable) {
        TimetableResponse res = new TimetableResponse();
        res.setId(timetable.getId());
        res.setPathCode(timetable.getPathCode());
        res.setStatus(timetable.getStatus().name());
        res.setTrainId(timetable.getTrain().getId());
        res.setArrivalTime(timetable.getArrivalTime());
        res.setDepartureTime(timetable.getDepartureTime());
        return res;
    }
	
	public Timetable toEntity(TimetableRequest req) {
        Train train = trainRepository.findById(req.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Train", req.getTrainId()));
        

        Timetable timetable = new Timetable();
        timetable.setTrain(train);
        timetable.setPathCode(req.getPathCode());
        timetable.setDepartureTime(req.getDepartureTime());
        timetable.setArrivalTime(req.getArrivalTime());
        timetable.setStatus(TimetableStatus.DRAFT);
        return timetable;
    }
	public Timetable toUpdate(Timetable existing, TimetableUpdateRequest req) {
        existing.setPathCode(req.getPathCode());
        existing.setDepartureTime(req.getDepartureTime());
        existing.setArrivalTime(req.getArrivalTime());
        return existing;
    }

}
