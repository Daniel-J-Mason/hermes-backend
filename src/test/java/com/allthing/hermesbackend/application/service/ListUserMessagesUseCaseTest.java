package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.Message;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.ServiceException;
import com.allthing.hermesbackend.application.ports.incoming.message.ListUserMessagesUseCase;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListUserMessagesUseCaseTest {
    
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
    
    private ListUserMessagesUseCase underTest;
    
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
    void getMessagesSuccessfully() {
        String username = "TestUser";
        User mockUser = new User(username, null, null);
        UUID firstMessageId = UUID.randomUUID();
        UUID secondMessageId = UUID.randomUUID();
        List<Message> expectedMessages = new ArrayList<>();
        expectedMessages.add(new Message(firstMessageId, "Title1", "body1", null, mockUser));
        expectedMessages.add(new Message(secondMessageId, "Title2", "body2", null, mockUser));
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(getMessagesByUsernamePort.getMessages(username)).thenReturn(expectedMessages);
        
        List<Message> actualMessages = underTest.getMessages(username);
        
        assertEquals(expectedMessages, actualMessages);
    }
    
    @Test
    void getMessagesShouldThrowsServiceExceptionIfNull() {
        String username = "TestUser";
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(getMessagesByUsernamePort.getMessages(username)).thenReturn(null);
        
        assertThrows(ServiceException.class, () -> underTest.getMessages(username));
    }
    
}
