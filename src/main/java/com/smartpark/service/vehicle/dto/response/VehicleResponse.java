package com.smartpark.service.vehicle.dto.response;

import com.smartpark.service.vehicle.entity.VehicleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleResponse {
    private String licensePlate;
    private VehicleType type;
    private String ownerName;
}
