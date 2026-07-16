package com.smartpark.service.lot.service;

import com.smartpark.service.lot.dto.request.ParkingLotRegistrationRequest;
import com.smartpark.service.lot.dto.response.OccupancyResponse;
import com.smartpark.service.lot.dto.response.ParkingLotResponse;
import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.lot.exception.ParkingLotAlreadyExistsException;
import com.smartpark.service.lot.exception.ParkingLotNotFoundException;
import com.smartpark.service.lot.repository.ParkingLotRepository;
import com.smartpark.service.session.repository.ParkingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingLotServiceTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @InjectMocks
    private ParkingLotService parkingLotService;

    @Test
    void registerParkingLotSuccessfully() {
        ParkingLotRegistrationRequest request = new ParkingLotRegistrationRequest();
        request.setLotId("LOT-001");
        request.setLocation("Main Entrance");
        request.setCapacity(10);
        request.setCostPerMinute(new BigDecimal("2.50"));

        when(parkingLotRepository.existsById("LOT-001")).thenReturn(false);

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLotId("LOT-001");
        parkingLot.setLocation("Main Entrance");
        parkingLot.setCapacity(10);
        parkingLot.setOccupiedSpaces(0);
        parkingLot.setCostPerMinute(new BigDecimal("2.50"));

        when(parkingLotRepository.save(any(ParkingLot.class))).thenReturn(parkingLot);

        ParkingLotResponse response = parkingLotService.registerParkingLot(request);

        assertEquals("LOT-001", response.getLotId());
        assertEquals("Main Entrance", response.getLocation());
        assertEquals(10, response.getCapacity());
        assertEquals(0, response.getOccupiedSpaces());

        verify(parkingLotRepository)
                .save(any(ParkingLot.class));
    }


    @Test
    void throwExceptionWhenParkingLotAlreadyExists() {

        ParkingLotRegistrationRequest request = new ParkingLotRegistrationRequest();
        request.setLotId("LOT-001");

        when(parkingLotRepository.existsById("LOT-001")).thenReturn(true);

        assertThrows(ParkingLotAlreadyExistsException.class,
                () -> parkingLotService.registerParkingLot(request)
        );

        verify(parkingLotRepository, times(0)).save(any());
    }


    @Test
    void getParkingLotOccupancySuccessfully() {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLotId("LOT-001");
        parkingLot.setLocation("Main Entrance");
        parkingLot.setCapacity(10);
        parkingLot.setOccupiedSpaces(3);

        when(parkingLotRepository.findById("LOT-001")).thenReturn(Optional.of(parkingLot));

        OccupancyResponse response = parkingLotService.getOccupancy("LOT-001");

        assertEquals("LOT-001", response.getLotId());
        assertEquals(3, response.getOccupiedSpaces());
        assertEquals(7, response.getAvailableSpaces());
    }


    @Test
    void throwExceptionWhenParkingLotDoesNotExist() {

        when(parkingLotRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ParkingLotNotFoundException.class,
                () -> parkingLotService.getOccupancy("INVALID"));
    }
}