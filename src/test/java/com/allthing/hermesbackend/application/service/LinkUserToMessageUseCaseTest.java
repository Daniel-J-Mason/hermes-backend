package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.message.MessageLinkException;
import com.allthing.hermesbackend.application.ports.incoming.message.LinkUserToMessageUseCase;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkUserToMessageUseCaseTest {
    
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
    
    private LinkUserToMessageUseCase underTest;
    
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
    public void testLinkUserSuccess() {
        String sendingUsername = "sender";
        String receivingUsername = "receiver";
        UUID messageId = UUID.randomUUID();
        User mockSender = new User(sendingUsername, null, null);
        User mockReceiver = new User(receivingUsername, null, null);
        
        when(findUserPort.findUser(sendingUsername)).thenReturn(mockSender);
        when(findUserPort.findUser(receivingUsername)).thenReturn(mockReceiver);
        when(areFriendsPort.areFriends(sendingUsername, receivingUsername)).thenReturn(true);
        when(checkMessageOwnershipPort.userOwnsMessage(sendingUsername, messageId)).thenReturn(true);
        when(createMessageLinkPort.createLink(messageId, receivingUsername)).thenReturn(true);
        
        boolean result = underTest.linkUser(sendingUsername, receivingUsername, messageId);
        
        verify(createMessageLinkPort, times(1)).createLink(messageId, receivingUsername);
        assertEquals(true, result);
    }
    
    @Test
    public void testLinkUserFailureNotFriends() {
        String sendingUsername = "sender";
        String receivingUsername = "receiver";
        UUID messageId = UUID.randomUUID();
        User mockSender = new User(sendingUsername, null, null);
        User mockReceiver = new User(receivingUsername, null, null);
        
        when(findUserPort.findUser(sendingUsername)).thenReturn(mockSender);
        when(findUserPort.findUser(receivingUsername)).thenReturn(mockReceiver);
        when(areFriendsPort.areFriends(sendingUsername, receivingUsername)).thenReturn(false);
        
        assertThrows(MessageLinkException.class, () -> underTest.linkUser(sendingUsername, receivingUsername, messageId));
    }
    
    @Test
    public void testLinkUserFailureNotMessageOwner() {
        String sendingUsername = "sender";
        String receivingUsername = "receiver";
        UUID messageId = UUID.randomUUID();
        User mockSender = new User(sendingUsername, null, null);
        User mockReceiver = new User(receivingUsername, null, null);
        
        when(findUserPort.findUser(sendingUsername)).thenReturn(mockSender);
        when(findUserPort.findUser(receivingUsername)).thenReturn(mockReceiver);
        when(areFriendsPort.areFriends(sendingUsername, receivingUsername)).thenReturn(true);
        when(checkMessageOwnershipPort.userOwnsMessage(sendingUsername, messageId)).thenReturn(false);
        
        assertThrows(MessageLinkException.class, () -> underTest.linkUser(sendingUsername, receivingUsername, messageId));
    }
    
}
