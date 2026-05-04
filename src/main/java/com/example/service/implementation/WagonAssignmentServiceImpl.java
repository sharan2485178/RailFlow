package com.example.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.dto.WagonAvailableResponse;
import com.example.enums.AssetAssignmentStatus;
import com.example.enums.AssetType;
import com.example.exception.BookingAlreadyFoundException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.WagonAssignmentMapper;
import com.example.model.Booking;
import com.example.model.Wagon;
import com.example.model.WagonAssignment;
import com.example.model.YardSlot;
import com.example.repository.BookingRepository;
import com.example.repository.WagonAssignmentRepository;
import com.example.repository.WagonRepository;
import com.example.repository.YardSlotRepository;
import com.example.service.WagonAssignmentService;

@Service
public class WagonAssignmentServiceImpl implements WagonAssignmentService {

	@Autowired
	private WagonAssignmentRepository wagonAssignmentRepository;

	@Autowired
	private WagonAssignmentMapper wagonAssignmentMapper;

	@Autowired
	private WagonRepository wagonRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private YardSlotRepository yardSlotRepository;

	public WagonAssignmentResponse assign(WagonAssignmentRequest req) {
		if (wagonAssignmentRepository.existsByBookingIdAndAssetAssignmentStatusIn(req.getBookingId(), List.of(
				AssetAssignmentStatus.PENDING, AssetAssignmentStatus.CONFIRMED, AssetAssignmentStatus.IN_TRANSIT))) {
			throw new BookingAlreadyFoundException("Booking " + req.getBookingId() + " is already assigned to a wagon");
		}
		Wagon wagon = wagonRepository.findById(req.getWagonId())
				.orElseThrow(() -> new EntityNotFoundException("Wagon with id" + req.getWagonId() + " is not found"));

		Booking booking = bookingRepository.findById(req.getBookingId())
				.orElseThrow(() -> new EntityNotFoundException("Booking id" + req.getBookingId() + "is not found"));

		if (wagon.getCapacityTon() < booking.getWeightTon()) {
			throw new IllegalStateException("Wagon capacity " + wagon.getCapacityTon() + " ton is insufficient "
					+ "for booking weight " + booking.getWeightTon() + " ton");
		}
		WagonAssignment wagonAssignment = wagonAssignmentMapper.toEntity(req);
		wagonAssignmentRepository.save(wagonAssignment);

		return wagonAssignmentMapper.toDto(wagonAssignment);
	}

	@Override
	public List<WagonAvailableResponse> getMatchingWagonsforBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new EntityNotFoundException("Booking with id" + bookingId + " is not found"));
		List<Long> assignedWagon = wagonAssignmentRepository
				.findByAssetAssignmentStatusIn(List.of(AssetAssignmentStatus.IN_TRANSIT, AssetAssignmentStatus.PENDING,
						AssetAssignmentStatus.CONFIRMED))
				.stream().map(x -> x.getWagon().getId()).collect(Collectors.toList());

		return wagonRepository.findAll().stream().filter(x -> !assignedWagon.contains(x.getId()))
				.filter(x -> x.getType().getAllowedCargoTypes().contains(booking.getCargoType())).map(x -> {
					Optional<YardSlot> slot = yardSlotRepository
					        .findByAssignedAssetTypeAndAssignedAssetId(AssetType.WAGON, x.getId());

					String yardName = slot.map(s -> s.getYard().getName()).orElse("Not in yard");
					Long slotId     = slot.map(YardSlot::getSlotId).orElse(null);

					return new WagonAvailableResponse(x.getId(), x.getSerialNumber(), x.getType(), yardName, slotId);

				}).collect(Collectors.toList());

	}
}
