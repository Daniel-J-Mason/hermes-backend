package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.Message;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.message.MessageUpdateException;
import com.allthing.hermesbackend.application.ports.incoming.message.UpdateMessageUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.AreFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageLinksPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageOwnershipPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.GetMessagesByUsernamePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.UpdateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateMessageUseCaseTest {
    
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
    private GetMessagesByUsernamePort getMessagesByUsernamePort;
    @Mock
    private UpdateMessagePort updateMessagePort;
    @Mock
    private AreFriendsPort areFriendsPort;
    @Mock
    private FindUserPort findUserPort;
    
    private UpdateMessageUseCase underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new MessagesService(
                checkMessageLinksPort,
                checkMessageOwnershipPort,
                createMessageLinkPort,
                createMessagePort,
                deleteMessagePort,
                deleteMessageLinkPort,
                getMessagesByUsernamePort,
                updateMessagePort,
                areFriendsPort,
                findUserPort
        );
    }
    
    @Test
    void shouldUpdateMessageWhenUserOwnsMessage() {
        UUID messageId = UUID.randomUUID();
        String username = "username";
        User mockUser = new User(username, null, null);
        Message testMessage = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(username, testMessage.publicId())).thenReturn(true);
        
        underTest.updateMessage(username, testMessage);
        
        verify(updateMessagePort, times(1)).updateMessage(username, testMessage);
    }
    
    @Test
    void shouldUpdateMessageWhenUserIsLinkedToMessage() {
        UUID messageId = UUID.randomUUID();
        String username = "username";
        User mockUser = new User(username, null, null);
        Message testMessage = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageLinksPort.checkUserLinkedToMessage(username, testMessage.publicId())).thenReturn(true);
        
        underTest.updateMessage(username, testMessage);
        
        verify(updateMessagePort, times(1)).updateMessage(username, testMessage);
    }
    
    @Test
    void shouldThrowExceptionWhenUserDoesNotOwnOrLinkedToMessage() {
        UUID messageId = UUID.randomUUID();
        String username = "username";
        User mockUser = new User(username, null, null);
        Message testMessage = new Message(messageId, "Title", "body", null, mockUser);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(username, testMessage.publicId())).thenReturn(false);
        when(checkMessageLinksPort.checkUserLinkedToMessage(username, testMessage.publicId())).thenReturn(false);
        
        assertThrows(MessageUpdateException.class, () -> underTest.updateMessage(username, testMessage));
    }
}
