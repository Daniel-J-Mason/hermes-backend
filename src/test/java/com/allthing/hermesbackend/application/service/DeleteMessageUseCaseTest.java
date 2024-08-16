package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.message.MessageCreationException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.message.DeleteMessageUseCase;
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

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteMessageUseCaseTest {
    
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
    
    private DeleteMessageUseCase underTest;
    
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
    public void testDeleteMessageSuccessful() {
        String username = "testUser";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(anyString(), any(UUID.class))).thenReturn(true);
        when(deleteMessagePort.deleteMessage(anyString(), any(UUID.class))).thenReturn(true);
        
        boolean result = underTest.deleteMessage(username, messageId);
        assertTrue(result);
        
        verify(deleteMessagePort, times(1)).deleteMessage(username, messageId);
    }
    
    @Test
    public void testDeleteMessageWhenUserNotExist() {
        String username = "testUser";
        UUID messageId = UUID.randomUUID();
        
        assertThrows(UserNotFoundException.class, () -> underTest.deleteMessage(username, messageId));
        
    }
    
    @Test
    public void testDeleteMessageWhenUserDoesNotOwnMessage() {
        String username = "testUser";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(anyString(), any(UUID.class))).thenReturn(false);
        
        assertThrows(MessageCreationException.class, () -> underTest.deleteMessage(username, messageId));
    }
}
