package com.smartpark.service.lot.controller;

import com.smartpark.service.lot.dto.request.ParkingLotRegistrationRequest;
import com.smartpark.service.lot.dto.response.OccupancyResponse;
import com.smartpark.service.lot.dto.response.ParkingLotResponse;
import com.smartpark.service.lot.dto.response.VehiclesInLotResponse;
import com.smartpark.service.lot.service.ParkingLotService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/parking-lot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity<ParkingLotResponse> registerParkingLot(@Valid @RequestBody ParkingLotRegistrationRequest request) {

        ParkingLotResponse response = parkingLotService.registerParkingLot(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{lotId}/occupancy")
    public ResponseEntity<OccupancyResponse> getOccupancy(@PathVariable String lotId) {
        OccupancyResponse response = parkingLotService.getOccupancy(lotId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lotId}/vehicles")
    public ResponseEntity<VehiclesInLotResponse> getVehiclesInLot(@PathVariable String lotId) {
        VehiclesInLotResponse response = parkingLotService.getVehiclesInLot(lotId);
        return ResponseEntity.ok(response);
    }
}
