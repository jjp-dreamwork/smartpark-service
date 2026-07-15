package com.smartpark.service.vehicle.controller;

import com.smartpark.service.vehicle.dto.request.VehicleRegistrationRequest;
import com.smartpark.service.vehicle.dto.response.VehicleResponse;
import com.smartpark.service.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRegistrationRequest request) {

        VehicleResponse response = vehicleService.registerVehicle(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
