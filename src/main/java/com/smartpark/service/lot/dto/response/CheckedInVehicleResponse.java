package com.smartpark.service.lot.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckedInVehicleResponse {
    private String licensePlate;
    private String type;
    private String ownerName;
    private String entryTime;
}
