package com.smartpark.service.lot.service;

import com.smartpark.service.lot.dto.request.ParkingLotRegistrationRequest;
import com.smartpark.service.lot.dto.response.ParkingLotResponse;
import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.exception.ParkingLotAlreadyExistsException;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public ParkingLotResponse registerParkingLot(ParkingLotRegistrationRequest request) {

        if (parkingLotRepository.existsById(request.getLotId())) {
            throw new ParkingLotAlreadyExistsException(
                    "ParkingLot with LotId-" + request.getLotId() + " is already registered.");
        }

        ParkingLot registerParkingLot = new ParkingLot(
                request.getLotId(),
                request.getLocation(),
                request.getCapacity(),
                0,
                request.getCostPerMinute()
        );

        ParkingLot registeredParkingLot = parkingLotRepository.save(registerParkingLot);

        return ParkingLotResponse.builder()
                .lotId(registeredParkingLot.getLotId())
                .location(registeredParkingLot.getLocation())
                .capacity(registeredParkingLot.getCapacity())
                .occupiedSpaces(registeredParkingLot.getOccupiedSpaces())
                .costPerMinute(registeredParkingLot.getCostPerMinute())
                .build();
    }

}
