package com.smartpark.service.vehicle.service;

import com.smartpark.service.vehicle.dto.request.VehicleRegistrationRequest;
import com.smartpark.service.vehicle.dto.response.VehicleResponse;
import com.smartpark.service.vehicle.entity.Vehicle;
import com.smartpark.service.vehicle.exception.VehicleAlreadyExistsException;
import com.smartpark.service.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse registerVehicle(VehicleRegistrationRequest request) {

        if (vehicleRepository.existsById(request.getLicensePlate())) {
            throw new VehicleAlreadyExistsException(
                    "Vehicle with LicensePlate-"+ request.getLicensePlate() +" is already registered.");
        }

        // normally MapStruct is used to reduce boilerplate
        Vehicle registeredVehicle = vehicleRepository.save(
                new Vehicle(
                        request.getLicensePlate(),
                        request.getType(),
                        request.getOwnerName())
        );

        return VehicleResponse.builder()
                .licensePlate(registeredVehicle.getLicensePlate())
                .type(registeredVehicle.getType())
                .ownerName(registeredVehicle.getOwnerName())
                .build();
    }
}
