package com.allthing.hermesbackend.application.ports.outgoing.message;

import com.allthing.hermesbackend.application.domain.Message;

import java.util.List;

public interface GetMessagesByUsername {
    List<Message> getMessages(String username);
}
