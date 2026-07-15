package com.smartpark.service.session.repository;

import com.smartpark.service.session.entity.ParkingSession;
import com.smartpark.service.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    boolean existsByVehicleAndExitTimeIsNull(Vehicle vehicle);
}
