package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestDeletionException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.friend.DeleteFriendRequestUseCase;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteFriendRequestUseCaseTest {
    
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
    
    private DeleteFriendRequestUseCase underTest;
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
    void deleteRequestShouldReturnTrueWhenDeletionSucceeds() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(deleteFriendRequestPort.deleteFriendRequest(user1.username(), user2.username())).thenReturn(true);
        
        boolean result = underTest.deleteRequest(user1.username(), user2.username());
        
        assertTrue(result, "Method deleteRequest should return false upon successful deletion");
    }
    
    @Test
    void deleteRequestShouldThrowExceptionIfSenderNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.deleteRequest(user1.username(), user2.username()));
    }
    
    @Test
    void deleteRequestShouldThrowExceptionIfReceiverNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.deleteRequest(user1.username(), user2.username()));
    }
    
    
    @Test
    void deleteRequestShouldThrowExceptionIfDeletionFails() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(deleteFriendRequestPort.deleteFriendRequest(user1.username(), user2.username())).thenReturn(false);
        
        assertThrows(FriendRequestDeletionException.class, () -> underTest.deleteRequest(user1.username(), user2.username()));
    }
}