package com.smartpark.service.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(length = 20)
    public String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public VehicleType type;

    @Column(nullable = false, length = 100)
    public String ownerName;
}
