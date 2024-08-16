package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.ServiceException;
import com.allthing.hermesbackend.application.exception.message.MessageLinkException;
import com.allthing.hermesbackend.application.ports.incoming.message.UnlinkUserFromMessageUseCase;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnlinkUserFromMessageUseCaseTest {
    
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
    
    private UnlinkUserFromMessageUseCase underTest;
    
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
    void testUnlinkUser() {
        String username = "username";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(username, messageId)).thenReturn(false);
        when(checkMessageLinksPort.checkUserLinkedToMessage(username, messageId)).thenReturn(true);
        when(deleteMessageLinkPort.deleteLink(messageId, username)).thenReturn(true);
        assertTrue(underTest.unLinkUser(messageId, username));
    }
    
    @Test
    void shouldThrowMessageLinkExceptionWhenUserNotOwnerOrLinked() {
        String username = "username";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(username, messageId)).thenReturn(false);
        when(checkMessageLinksPort.checkUserLinkedToMessage(username, messageId)).thenReturn(false);
        Assertions.assertThrows(MessageLinkException.class, () -> underTest.unLinkUser(messageId, username));
    }
    
    @Test
    void shouldThrowExceptionWhenUserIsOwner() {
        String username = "username";
        UUID messageId = UUID.randomUUID();
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(checkMessageOwnershipPort.userOwnsMessage(username, messageId)).thenReturn(true);
        Assertions.assertThrows(ServiceException.class, () -> underTest.unLinkUser(messageId, username));
    }
}
