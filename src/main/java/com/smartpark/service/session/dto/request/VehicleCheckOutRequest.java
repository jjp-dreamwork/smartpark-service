package com.smartpark.service.session.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleCheckOutRequest {

    @NotBlank(message = "LicensePlate is required.")
    @Pattern(
        regexp = "^[A-Za-z0-9-]+$",
        message = "License plate may only contain letters, numbers, and dashes.")
    private String licensePlate;
}
