package com.smartpark.service.session.exception;

public class ActiveParkingSessionNotFoundException extends RuntimeException {
    public ActiveParkingSessionNotFoundException(String message) {
        super(message);
    }
}
