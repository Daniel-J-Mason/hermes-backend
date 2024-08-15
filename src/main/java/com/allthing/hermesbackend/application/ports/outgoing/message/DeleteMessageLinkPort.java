package com.allthing.hermesbackend.application.ports.outgoing.message;

import java.util.UUID;

public interface DeleteMessageLinkPort {
    boolean deleteLink(UUID publicMessageId, String username);
    
}
