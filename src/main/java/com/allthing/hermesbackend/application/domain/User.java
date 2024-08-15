package com.allthing.hermesbackend.application.domain;

import java.util.UUID;

public record User(UUID uuid, String username, PhoneNumber phoneNumber, Email email) {
}
