package com.smartpark.service.session.service;

import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.exception.ParkingLotNotFoundException;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import com.smartpark.service.session.dto.request.VehicleCheckInRequest;
import com.smartpark.service.session.dto.request.VehicleCheckOutRequest;
import com.smartpark.service.session.dto.response.VehicleCheckInResponse;
import com.smartpark.service.session.dto.response.VehicleCheckOutResponse;
import com.smartpark.service.session.entity.ParkingSession;
import com.smartpark.service.session.exception.ActiveParkingSessionNotFoundException;
import com.smartpark.service.session.exception.ParkingLotFullException;
import com.smartpark.service.session.exception.VehicleAlreadyCheckedInException;
import com.smartpark.service.session.repository.ParkingSessionRepository;
import com.smartpark.service.shared.config.SmartParkProperties;
import com.smartpark.service.vehicle.entity.Vehicle;
import com.smartpark.service.vehicle.exception.VehicleNotFoundException;
import com.smartpark.service.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParkingSessionService {

    private final SmartParkProperties smartParkProperties;
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

    @Transactional
    public VehicleCheckOutResponse checkOut(VehicleCheckOutRequest request) {
        Vehicle vehicle = vehicleRepository
                .findById(request.getLicensePlate())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found."));

        ParkingSession parkingSession = parkingSessionRepository
                .findByVehicleAndExitTimeIsNull(vehicle)
                .orElseThrow(() -> new ActiveParkingSessionNotFoundException("Vehicle is not currently checked-in."));

        return this.performCheckOut(parkingSession);
    }

    @Transactional
    public void autoCheckoutExpiredSessions() {
        log.info("Starting auto checkout process.");

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(smartParkProperties.getTimeoutMinutes());

        List<ParkingSession> sessions = parkingSessionRepository.findByExitTimeIsNullAndEntryTimeBefore(cutoff);

        log.info("Found {} expired parking session(s).", sessions.size());

        for (ParkingSession session : sessions) {
            try {
                this.performCheckOut(session);
                log.info("Auto checked out parking session {}.", session.getId());
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.info("Parking session {} was already processed.", session.getId());
            }
        }
    }

    private VehicleCheckOutResponse performCheckOut(ParkingSession parkingSession) {
        LocalDateTime exitTime = LocalDateTime.now();
        parkingSession.setExitTime(exitTime);

        long parkedMinutes = calculateParkedMinutes(
                parkingSession.getEntryTime(),
                exitTime
        );

        BigDecimal parkingFee = calculateParkingFee(
                parkingSession.getParkingLot().getCostPerMinute(),
                parkedMinutes
        );

        parkingSession.setParkingFee(parkingFee);

        ParkingLot parkingLot = parkingSession.getParkingLot();

        // an edge case and normally if an active parking session was found, the occupiedSpaces should not be zero
        if (parkingLot.getOccupiedSpaces() <= 0) {
            throw new IllegalStateException("Parking lot occupied spaces cannot be negative.");
        }

        parkingLot.setOccupiedSpaces(
                parkingLot.getOccupiedSpaces() - 1
        );

        parkingSessionRepository.save(parkingSession);
        parkingLotRepository.save(parkingLot);

        return VehicleCheckOutResponse.builder()
                .sessionId(parkingSession.getId())
                .licensePlate(parkingSession.getVehicle().getLicensePlate())
                .lotId(parkingLot.getLotId())
                .entryTime(parkingSession.getEntryTime())
                .exitTime(exitTime)
                .parkedMinutes(parkedMinutes)
                .parkingFee(parkingFee)
                .build();
    }

    private long calculateParkedMinutes(LocalDateTime entryTime, LocalDateTime exitTime) {

        Duration duration = Duration.between(entryTime, exitTime);

        long minutes = duration.toMinutes();

        if (duration.getSeconds() % 60 != 0) {
            minutes++;
        }

        return Math.max(1, minutes);
    }

    private BigDecimal calculateParkingFee(BigDecimal costPerMinute, long parkedMinutes) {
        return costPerMinute.multiply(
                BigDecimal.valueOf(parkedMinutes)
        );
    }
}
