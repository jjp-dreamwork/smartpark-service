package com.smartpark.service.session.controller;

import com.smartpark.service.session.dto.request.VehicleCheckInRequest;
import com.smartpark.service.session.dto.response.VehicleCheckInResponse;
import com.smartpark.service.session.service.ParkingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parking-session")
public class ParkingSessionController {

    private final ParkingSessionService parkingSessionService;

    @PostMapping("/check-in")
    public ResponseEntity<VehicleCheckInResponse> checkIn(
            @Valid @RequestBody VehicleCheckInRequest request) {

        VehicleCheckInResponse response = parkingSessionService.checkIn(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
