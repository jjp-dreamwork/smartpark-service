package com.smartpark.service.lot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="parking_lot")
public class ParkingLot {

    @Id
    @Column(nullable = false, length = 50)
    private String lotId;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer occupiedSpaces = 0;

    @Column(nullable = false)
    private BigDecimal costPerMinute;

    @Version
    private Long version;
}
