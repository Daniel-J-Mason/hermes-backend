package com.allthing.hermesbackend.application.ports.incoming.message;

import java.util.UUID;

public interface LinkUserToMessageUseCase {
    boolean linkUser(String senderUsername, String receiverUsername, UUID publicMessageId);
}
