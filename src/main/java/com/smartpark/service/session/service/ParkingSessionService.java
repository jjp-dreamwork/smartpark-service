package com.smartpark.service.session.service;

import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.exception.ParkingLotNotFoundException;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import com.smartpark.service.session.dto.request.VehicleCheckInRequest;
import com.smartpark.service.session.dto.response.VehicleCheckInResponse;
import com.smartpark.service.session.entity.ParkingSession;
import com.smartpark.service.session.exception.ParkingLotFullException;
import com.smartpark.service.session.exception.VehicleAlreadyCheckedInException;
import com.smartpark.service.session.repository.ParkingSessionRepository;
import com.smartpark.service.vehicle.entity.Vehicle;
import com.smartpark.service.vehicle.exception.VehicleNotFoundException;
import com.smartpark.service.vehicle.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingSessionService {

    private final VehicleRepository vehicleRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    @Transactional
    public VehicleCheckInResponse checkIn(VehicleCheckInRequest request) {
        Vehicle vehicle = vehicleRepository
                .findById(request.getLicensePlate())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found."));

        ParkingLot parkingLot = parkingLotRepository
                .findById(request.getLotId())
                .orElseThrow(() -> new ParkingLotNotFoundException("ParkingLot not found."));

        if (parkingSessionRepository.existsByVehicleAndExitTimeIsNull(vehicle)) {
            throw new VehicleAlreadyCheckedInException("Vehicle is already checked-in.");
        }

        if (parkingLot.getOccupiedSpaces() >= parkingLot.getCapacity()) {
            throw new ParkingLotFullException("Parking lot is full.");
        }

        parkingLot.setOccupiedSpaces(
                parkingLot.getOccupiedSpaces() + 1
        );

        ParkingSession parkingSession = new ParkingSession();
        parkingSession.setVehicle(vehicle);
        parkingSession.setParkingLot(parkingLot);
        parkingSession.setEntryTime(LocalDateTime.now());

        ParkingSession checkedInSession = parkingSessionRepository.save(parkingSession);

        parkingLotRepository.save(parkingLot);

        return VehicleCheckInResponse.builder()
                .sessionId(checkedInSession.getId())
                .licensePlate(vehicle.getLicensePlate())
                .lotId(parkingLot.getLotId())
                .entryTime(checkedInSession.getEntryTime())
                .build();
    }

}
