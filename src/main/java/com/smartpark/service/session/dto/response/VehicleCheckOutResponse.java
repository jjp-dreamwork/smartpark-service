package com.smartpark.service.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class VehicleCheckOutResponse {
    private Long sessionId;
    private String licensePlate;
    private String lotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long parkedMinutes;
    private BigDecimal parkingFee;
}
