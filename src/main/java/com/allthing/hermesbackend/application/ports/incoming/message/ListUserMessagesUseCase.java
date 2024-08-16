package com.allthing.hermesbackend.application.ports.incoming.message;

import com.allthing.hermesbackend.application.domain.Message;

import java.util.List;

public interface ListUserMessagesUseCase {
    List<Message> getMessages(String username);
}
