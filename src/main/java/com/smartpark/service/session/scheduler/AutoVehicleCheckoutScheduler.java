package com.smartpark.service.session.scheduler;

import com.smartpark.service.session.service.ParkingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoVehicleCheckoutScheduler {

    private final ParkingSessionService parkingSessionService;

    @Scheduled(fixedRateString = "${smartpark.auto-checkout.fixed-rate-ms}")
    public void autoCheckout() {
        parkingSessionService.autoCheckoutExpiredSessions();
    }
}
