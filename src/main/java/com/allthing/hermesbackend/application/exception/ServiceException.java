package com.allthing.hermesbackend.application.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
