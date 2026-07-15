package com.smartpark.service.vehicle.dto.request;

import com.smartpark.service.vehicle.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VehicleRegistrationRequest {
    @NotBlank(message = "LicensePlate is required.")
    @Pattern(
        regexp = "^[A-Za-z0-9-]+$",
        message = "License plate may only contain letters, numbers, and dashes.")
    public String licensePlate;

    @NotNull(message = "VehicleType is required.")
    public VehicleType type;

    @NotBlank(message = "OwnerName is required.")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Owner name may only contain letters and spaces."
    )
    public String ownerName;
}
