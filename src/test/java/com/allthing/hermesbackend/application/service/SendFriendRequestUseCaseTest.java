package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.FriendRequest;
import com.allthing.hermesbackend.application.domain.Status;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestCreationException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.friend.SendFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.CreateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.DeleteFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.LookupFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.UpdateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendFriendRequestUseCaseTest {
    
    @Mock
    private CreateFriendRequestPort createFriendRequestPort;
    @Mock
    private DeleteFriendRequestPort deleteFriendRequestPort;
    @Mock
    private UpdateFriendRequestPort updateFriendRequestPort;
    @Mock
    private LookupFriendRequestPort lookupFriendRequestPort;
    @Mock
    private FindUserPort findUserPort;
    
    private SendFriendRequestUseCase underTest;
    private User user1;
    private User user2;
    
    @BeforeEach
    void setUp() {
        underTest = new FriendRequestService(
                createFriendRequestPort,
                deleteFriendRequestPort,
                updateFriendRequestPort,
                lookupFriendRequestPort,
                findUserPort
        );
        
        user1 = new User("username1", null, null);
        user2 = new User("username2", null, null);
    }
    
    @Test
    void sendRequestShouldReturnCreatedRequestWhenCreationSucceeds() {
        FriendRequest request = new FriendRequest(user1, user2, Status.PENDING);
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(createFriendRequestPort.createRequest(any())).thenReturn(request);
        
        FriendRequest result = underTest.sendRequest(user1.username(), user2.username());
        
        assertEquals(result, request, "Method sendRequest should return the created FriendRequest");
    }
    
    @Test
    void sendRequestShouldThrowExceptionIfSenderNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.sendRequest(user1.username(), user2.username()));
    }
    
    @Test
    void sendRequestShouldThrowExceptionIfReceiverNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.sendRequest(user1.username(), user2.username()));
    }
    
    
    @Test
    void sendRequestShouldThrowExceptionIfCreationFails() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(createFriendRequestPort.createRequest(any())).thenReturn(null);
        
        assertThrows(FriendRequestCreationException.class, () -> underTest.sendRequest(user1.username(), user2.username()));
    }

}