package com.smartpark.service.lot.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OccupancyResponse {
    private String lotId;
    private String location;
    private Integer capacity;
    private Integer occupiedSpaces;
    private Integer availableSpaces;
}
