package com.smartpark.service.lot.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkingLotRegistrationRequest {

    @NotBlank(message = "LotId is required.")
    @Size(max = 50, message = "LotId must not exceed 50 characters.")
    private String lotId;

    @NotBlank(message = "Location is required.")
    private String location;

    @NotNull(message = "Capacity is required.")
    @Min(value = 1, message = "Capacity must be at least 1.")
    private Integer capacity;

    @NotNull(message = "Cost per minute is required.")
    @DecimalMin(value = "0.01", message = "Cost per minute must be greater than 0.")
    private BigDecimal costPerMinute;
}
