package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.Message;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.message.MessageCreationException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.message.CreateMessageUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.AreFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageLinksPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageOwnershipPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.GetMessagesByUsername;
import com.allthing.hermesbackend.application.ports.outgoing.message.UpdateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateMessageUseCaseTest {
    
    @Mock
    private CheckMessageLinksPort checkMessageLinksPort;
    @Mock
    private CheckMessageOwnershipPort checkMessageOwnershipPort;
    @Mock
    private CreateMessageLinkPort createMessageLinkPort;
    @Mock
    private CreateMessagePort createMessagePort;
    @Mock
    private DeleteMessagePort deleteMessagePort;
    @Mock
    private DeleteMessageLinkPort deleteMessageLinkPort;
    @Mock
    private GetMessagesByUsername getMessagesByUsername;
    @Mock
    private UpdateMessagePort updateMessagePort;
    @Mock
    private AreFriendsPort areFriendsPort;
    @Mock
    private FindUserPort findUserPort;
    
    private CreateMessageUseCase underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new MessagesService(
                checkMessageLinksPort,
                checkMessageOwnershipPort,
                createMessageLinkPort,
                createMessagePort,
                deleteMessagePort,
                deleteMessageLinkPort,
                getMessagesByUsername,
                updateMessagePort,
                areFriendsPort,
                findUserPort
        );
    }
    
    @Test
    void shouldCreateMessage() {
        String username = "testUsername";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        Message messageToCreate = new Message(messageId, "Title", "body", null, mockUser);
        Message createdMessage = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(createMessagePort.createMessage(username, messageToCreate))
                .thenReturn(createdMessage);
        
        
        Message result = underTest.createMessage(username, messageToCreate);
        
        
        assertEquals(createdMessage, result);
    }
    
    @Test
    void shouldThrowWhenMessageNotCreated() {
        String username = "testUsername";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        Message messageToCreate = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(createMessagePort.createMessage(username, messageToCreate))
                .thenReturn(null);
        
        assertThrows(MessageCreationException.class, () -> underTest.createMessage(username, messageToCreate));
    }
    
    @Test
    void shouldThrowWhenUserNotFound() {
        String username = "testUsername";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        Message messageToCreate = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> underTest.createMessage(username, messageToCreate));
    }
    
}
