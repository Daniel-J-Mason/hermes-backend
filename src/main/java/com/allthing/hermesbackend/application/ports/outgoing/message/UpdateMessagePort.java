package com.allthing.hermesbackend.application.ports.outgoing.message;

import com.allthing.hermesbackend.application.domain.Message;

public interface UpdateMessagePort {
    Message updateMessage(Message message);
    
}
