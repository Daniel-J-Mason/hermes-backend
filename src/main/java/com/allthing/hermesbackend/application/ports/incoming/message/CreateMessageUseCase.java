package com.allthing.hermesbackend.application.ports.incoming.message;

import com.allthing.hermesbackend.application.domain.Message;

public interface CreateMessageUseCase {
    Message createMessage(String username, Message message);
}
