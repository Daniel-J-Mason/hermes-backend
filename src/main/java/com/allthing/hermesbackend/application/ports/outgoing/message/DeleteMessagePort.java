package com.allthing.hermesbackend.application.ports.outgoing.message;

import java.util.UUID;

public interface DeleteMessagePort {
    boolean deleteMessage(String username, UUID publicId);
    
}
