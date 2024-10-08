package com.allthing.hermesbackend.application.ports.outgoing.message;

import java.util.UUID;

//Update user_message to link or unlink a user from a message
public interface CreateMessageLinkPort {
    boolean createLink(UUID publicMessageId, String username);
}
