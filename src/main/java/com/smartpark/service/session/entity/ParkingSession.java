package com.smartpark.service.session.entity;

import com.smartpark.service.lot.entity.ParkingLot;
import com.smartpark.service.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parking_session")
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "license_plate")
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;
    private BigDecimal parkingFee;
}
