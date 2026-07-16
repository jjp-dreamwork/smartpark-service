package com.smartpark.service.vehicle.service;

import com.smartpark.service.vehicle.dto.request.VehicleRegistrationRequest;
import com.smartpark.service.vehicle.dto.response.VehicleResponse;
import com.smartpark.service.vehicle.entity.Vehicle;
import com.smartpark.service.vehicle.entity.VehicleType;
import com.smartpark.service.vehicle.exception.VehicleAlreadyExistsException;
import com.smartpark.service.vehicle.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void registerVehicleSuccessfully() {
        VehicleRegistrationRequest request = new VehicleRegistrationRequest();
        request.setLicensePlate("CAR-001");
        request.setType(VehicleType.CAR);
        request.setOwnerName("John Jason");

        when(vehicleRepository.existsById("CAR-001")).thenReturn(false);

        Vehicle vehicle = new Vehicle("CAR-001", VehicleType.CAR, "John Jason");

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleResponse response = vehicleService.registerVehicle(request);
        assertEquals("CAR-001", response.getLicensePlate());
        assertEquals(VehicleType.CAR, response.getType());
        assertEquals("John Jason", response.getOwnerName());

        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void throwExceptionWhenVehicleAlreadyExists() {

        VehicleRegistrationRequest request = new VehicleRegistrationRequest();
        request.setLicensePlate("CAR-001");

        when(vehicleRepository.existsById("CAR-001"))
                .thenReturn(true);

        assertThrows(VehicleAlreadyExistsException.class,
            () -> vehicleService.registerVehicle(request));

        verify(vehicleRepository, times(0)).save(any());
    }
}
