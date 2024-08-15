package com.allthing.hermesbackend.application.ports.outgoing.message;

import java.util.UUID;

public interface CheckMessageOwnershipPort {
    boolean userOwnsMessage(String username, UUID publicMessageId);
}
