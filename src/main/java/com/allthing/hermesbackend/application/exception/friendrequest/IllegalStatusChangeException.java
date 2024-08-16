package com.allthing.hermesbackend.application.exception.friendrequest;

public class IllegalStatusChangeException extends RuntimeException {
    public IllegalStatusChangeException(String message) {
        super(message);
    }
}
