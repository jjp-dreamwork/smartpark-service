package com.smartpark.service.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VehicleCheckInResponse {
    private Long sessionId;
    private String licensePlate;
    private String lotId;
    private LocalDateTime entryTime;
}
