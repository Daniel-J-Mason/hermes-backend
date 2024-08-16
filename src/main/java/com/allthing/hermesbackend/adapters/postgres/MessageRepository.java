package com.allthing.hermesbackend.adapters.postgres;

import com.allthing.hermesbackend.application.domain.Message;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageLinksPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageOwnershipPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.GetMessagesByUsernamePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.UpdateMessagePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepository implements CheckMessageLinksPort, CheckMessageOwnershipPort, CreateMessageLinkPort,
        CreateMessagePort, DeleteMessageLinkPort, DeleteMessagePort, GetMessagesByUsernamePort, UpdateMessagePort {
    @Override
    public boolean checkUserLinkedToMessage(String username, UUID publicMessageId) {
        return false;
    }
    
    @Override
    public boolean userOwnsMessage(String username, UUID publicMessageId) {
        return false;
    }
    
    @Override
    public boolean createLink(UUID publicMessageId, String username) {
        return false;
    }
    
    @Override
    public Message createMessage(String username, Message message) {
        return null;
    }
    
    @Override
    public boolean deleteLink(UUID publicMessageId, String username) {
        return false;
    }
    
    @Override
    public boolean deleteMessage(String username, UUID publicId) {
        return false;
    }
    
    @Override
    public List<Message> getMessages(String username) {
        return List.of();
    }
    
    @Override
    public Message updateMessage(String username, Message message) {
        return null;
    }
}
