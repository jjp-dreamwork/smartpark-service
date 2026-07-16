package com.smartpark.service.lot.service;

import com.smartpark.service.lot.dto.request.ParkingLotRegistrationRequest;
import com.smartpark.service.lot.dto.response.CheckedInVehicleResponse;
import com.smartpark.service.lot.dto.response.OccupancyResponse;
import com.smartpark.service.lot.dto.response.ParkingLotResponse;
import com.smartpark.service.lot.dto.response.VehiclesInLotResponse;
import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.exception.ParkingLotAlreadyExistsException;
import com.smartpark.service.lot.exception.ParkingLotNotFoundException;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import com.smartpark.service.session.repository.ParkingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    public ParkingLotResponse registerParkingLot(ParkingLotRegistrationRequest request) {

        if (parkingLotRepository.existsById(request.getLotId())) {
            throw new ParkingLotAlreadyExistsException(
                    "ParkingLot with LotId-" + request.getLotId() + " is already registered.");
        }

        ParkingLot registerParkingLot = new ParkingLot();
        registerParkingLot.setLotId(request.getLotId());
        registerParkingLot.setLocation(request.getLocation());
        registerParkingLot.setCapacity(request.getCapacity());
        registerParkingLot.setCostPerMinute(request.getCostPerMinute());

        ParkingLot registeredParkingLot = parkingLotRepository.save(registerParkingLot);

        return ParkingLotResponse.builder()
                .lotId(registeredParkingLot.getLotId())
                .location(registeredParkingLot.getLocation())
                .capacity(registeredParkingLot.getCapacity())
                .occupiedSpaces(registeredParkingLot.getOccupiedSpaces())
                .costPerMinute(registeredParkingLot.getCostPerMinute())
                .build();
    }

    public OccupancyResponse getOccupancy(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ParkingLotNotFoundException("Parking lot not found."));

        Integer availableSpaces = parkingLot.getCapacity() - parkingLot.getOccupiedSpaces();

        return OccupancyResponse.builder()
                .lotId(parkingLot.getLotId())
                .location(parkingLot.getLocation())
                .capacity(parkingLot.getCapacity())
                .occupiedSpaces(parkingLot.getOccupiedSpaces())
                .availableSpaces(availableSpaces)
                .build();
    }

    public VehiclesInLotResponse getVehiclesInLot(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId)
                .orElseThrow(() -> new ParkingLotNotFoundException("Parking lot not found."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<CheckedInVehicleResponse> vehicles = parkingSessionRepository
                .findByParkingLotAndExitTimeIsNull(parkingLot)
                .stream()
                .map(session ->
                        CheckedInVehicleResponse.builder()
                            .licensePlate(session.getVehicle().getLicensePlate())
                            .type(session.getVehicle().getType().name())
                            .ownerName(session.getVehicle().getOwnerName())
                            .entryTime(session.getEntryTime().format(formatter))
                            .build())
                .collect(Collectors.toList());

        return VehiclesInLotResponse.builder()
                .lotId(parkingLot.getLotId())
                .location(parkingLot.getLocation())
                .vehicles(vehicles)
                .totalVehicles(vehicles.size())
                .build();
    }

}
