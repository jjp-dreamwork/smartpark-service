package com.smartpark.service.session.service;

import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import com.smartpark.service.session.dto.request.VehicleCheckInRequest;
import com.smartpark.service.session.dto.response.VehicleCheckInResponse;
import com.smartpark.service.session.entity.ParkingSession;
import com.smartpark.service.session.repository.ParkingSessionRepository;
import com.smartpark.service.shared.config.SmartParkProperties;
import com.smartpark.service.vehicle.entity.Vehicle;
import com.smartpark.service.vehicle.entity.VehicleType;
import com.smartpark.service.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSessionServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private SmartParkProperties smartParkProperties;

    @InjectMocks
    private ParkingSessionService parkingSessionService;

    private Vehicle vehicle;
    private ParkingLot parkingLot;

    @BeforeEach
    void setup(){

        vehicle = new Vehicle(
                "CAR-001",
                VehicleType.CAR,
                "John Jason"
        );

        parkingLot = new ParkingLot();
        parkingLot.setLotId("LOT-001");
        parkingLot.setLocation("Main Entrance");
        parkingLot.setCapacity(10);
        parkingLot.setOccupiedSpaces(0);
        parkingLot.setCostPerMinute(new BigDecimal("2.50"));
    }

    @Test
    void checkInVehicleSuccessfully(){
        VehicleCheckInRequest request = new VehicleCheckInRequest();
        request.setLicensePlate("CAR-001");
        request.setLotId("LOT-001");

        when(vehicleRepository.findById("CAR-001")).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(parkingLot));
        when(parkingSessionRepository.existsByVehicleAndExitTimeIsNull(vehicle)).thenReturn(false);

        ParkingSession session = new ParkingSession();
        session.setId(1L);
        session.setVehicle(vehicle);
        session.setParkingLot(parkingLot);
        session.setEntryTime(LocalDateTime.now());

        when(parkingSessionRepository.save(any())).thenReturn(session);

        VehicleCheckInResponse response = parkingSessionService.checkIn(request);

        assertEquals("CAR-001", response.getLicensePlate());
        assertEquals("LOT-001", response.getLotId());
        assertEquals(1, parkingLot.getOccupiedSpaces());

        verify(parkingSessionRepository).save(any(ParkingSession.class));
        verify(parkingLotRepository).save(parkingLot);
    }

}
