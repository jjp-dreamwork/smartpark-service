package com.smartpark.service.lot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ParkingLotResponse {
    private String lotId;
    private String location;
    private Integer capacity;
    private Integer occupiedSpaces;
    private BigDecimal costPerMinute;
}
