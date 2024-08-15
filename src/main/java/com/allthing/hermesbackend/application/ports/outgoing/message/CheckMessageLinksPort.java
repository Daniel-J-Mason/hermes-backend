package com.allthing.hermesbackend.application.ports.outgoing.message;

import java.util.UUID;

public interface CheckMessageLinksPort {
    boolean checkUserLinkedToMessage(String username, UUID publicMessageId);
}
