package com.allthing.hermesbackend.application.ports.incoming.message;

import java.util.UUID;

public interface DeleteMessageUseCase {
    boolean deleteMessage(String username, UUID publicId);
}
