package com.smartpark.service.session.repository;

import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.session.entity.ParkingSession;
import com.smartpark.service.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    boolean existsByVehicleAndExitTimeIsNull(Vehicle vehicle);
    Optional<ParkingSession> findByVehicleAndExitTimeIsNull(Vehicle vehicle);
    List<ParkingSession> findByExitTimeIsNullAndEntryTimeBefore(LocalDateTime cutoff);
    List<ParkingSession> findByParkingLotAndExitTimeIsNull(ParkingLot parkingLot);
}
