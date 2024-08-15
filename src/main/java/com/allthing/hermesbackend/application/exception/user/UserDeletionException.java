package com.allthing.hermesbackend.application.exception.user;

public class UserDeletionException extends RuntimeException {
    public UserDeletionException(String message) {
        super(message);
    }
}
