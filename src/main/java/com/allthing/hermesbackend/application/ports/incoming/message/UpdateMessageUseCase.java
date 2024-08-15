package com.allthing.hermesbackend.application.ports.incoming.message;

import com.allthing.hermesbackend.application.domain.Message;

public interface UpdateMessageUseCase {
    Message updateMessage(String username, Message message);
}
