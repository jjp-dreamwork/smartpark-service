package com.smartpark.service.lot.exception;

public class ParkingLotAlreadyExistsException extends RuntimeException {
    public ParkingLotAlreadyExistsException(String message) {
        super(message);
    }
}
