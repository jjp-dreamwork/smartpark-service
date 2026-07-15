package com.smartpark.service.session.exception;

public class VehicleAlreadyCheckedInException extends RuntimeException {
    public VehicleAlreadyCheckedInException(String message) {
        super(message);
    }
}
