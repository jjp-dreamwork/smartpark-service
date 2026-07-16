package com.smartpark.service.lot.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VehiclesInLotResponse {
    private String lotId;
    private String location;
    private List<CheckedInVehicleResponse> vehicles;
    private Integer totalVehicles;
}
